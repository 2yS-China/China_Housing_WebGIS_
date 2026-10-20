"""
计算全局 Moran's I 和局部 LISA 并写入数据库
依赖: pip install psycopg2 numpy scipy
"""
import psycopg2
import numpy as np
from scipy import sparse
from scipy.stats import norm
import sys

from housing_config import DB_CONFIG as DB_CONFIG

def get_connection():
    return psycopg2.connect(**DB_CONFIG)

def fetch_city_prices(conn, year, month):
    """获取指定年月有经纬度和价格的城市数据"""
    sql = """
        SELECT c.city_code, c.lon, c.lat, mp.price
        FROM t_monthly_price mp
        JOIN t_city c ON c.city_code = mp.city_code
        WHERE mp.year = %s AND mp.month = %s
          AND c.lon IS NOT NULL AND c.lat IS NOT NULL
          AND mp.price IS NOT NULL AND mp.price > 0
        ORDER BY c.city_code
    """
    with conn.cursor() as cur:
        cur.execute(sql, (year, month))
        rows = cur.fetchall()
    return rows

def build_distance_weights(lons, lats, k=8):
    """构建 K 近邻空间权重矩阵 (行标准化)"""
    n = len(lons)
    coords = np.column_stack([lons, lats])
    # 计算欧氏距离矩阵
    diff = coords[:, np.newaxis, :] - coords[np.newaxis, :, :]
    dist = np.sqrt((diff ** 2).sum(axis=2))

    W = np.zeros((n, n))
    for i in range(n):
        d = dist[i].copy()
        d[i] = np.inf
        neighbors = np.argsort(d)[:k]
        W[i, neighbors] = 1.0

    # 行标准化
    row_sums = W.sum(axis=1, keepdims=True)
    row_sums[row_sums == 0] = 1
    W = W / row_sums
    return W

def compute_global_moran(prices, W):
    """计算全局 Moran's I"""
    n = len(prices)
    z = prices - prices.mean()
    S0 = W.sum()
    num = n * float(z @ W @ z)
    den = S0 * float(z @ z)
    I = num / den if den != 0 else 0

    # 期望和方差 (假设正态近似)
    E_I = -1.0 / (n - 1)
    S1 = 0.5 * ((W + W.T) ** 2).sum()
    S2 = ((W.sum(axis=1) + W.sum(axis=0)) ** 2).sum()
    z2 = float((z ** 2).sum())
    z4 = float((z ** 4).sum())
    D = n * z4 / (z2 ** 2) if z2 != 0 else 0

    A = n * ((n * n - 3 * n + 3) * S1 - n * S2 + 3 * S0 * S0)
    B = D * ((n * n - n) * S1 - 2 * n * S2 + 6 * S0 * S0)
    C = (n - 1) * (n - 2) * (n - 3) * S0 * S0

    E_I2 = (A - B) / C if C != 0 else 0
    var_I = E_I2 - E_I ** 2
    std_I = np.sqrt(var_I) if var_I > 0 else 1e-10
    z_score = (I - E_I) / std_I
    p_value = 2 * (1 - norm.cdf(abs(z_score)))

    return round(float(I), 6), round(float(z_score), 6), round(float(p_value), 6)

def compute_lisa(prices, W):
    """计算局部 Moran's I (LISA)"""
    n = len(prices)
    z = prices - prices.mean()
    m2 = float((z ** 2).sum()) / n

    local_I = np.zeros(n)
    for i in range(n):
        lag = 0.0
        for j in range(n):
            lag += W[i, j] * z[j]
        local_I[i] = (z[i] / m2) * lag if m2 != 0 else 0

    # 伪 p 值简化：基于正态近似
    E_Ii = -W.sum(axis=1) / (n - 1)
    var_approx = np.full(n, 0.01)  # 简化方差估计
    z_scores = (local_I - E_Ii) / np.sqrt(var_approx + 1e-10)
    p_values = 2 * (1 - norm.cdf(np.abs(z_scores)))

    # 分类 LISA 聚类
    cluster_types = []
    mean_price = prices.mean()
    for i in range(n):
        if p_values[i] > 0.05:
            cluster_types.append('NS')
        else:
            lag_val = sum(W[i, j] * prices[j] for j in range(n))
            high_self = prices[i] > mean_price
            high_lag = lag_val > mean_price
            if high_self and high_lag:
                cluster_types.append('HH')
            elif not high_self and not high_lag:
                cluster_types.append('LL')
            elif high_self and not high_lag:
                cluster_types.append('HL')
            else:
                cluster_types.append('LH')

    return local_I, p_values, cluster_types

def save_results(conn, year, month, global_I, z_score, p_value, city_codes, local_I_arr, p_values_arr, cluster_types):
    """写入数据库"""
    with conn.cursor() as cur:
        # 删除旧记录
        cur.execute("""
            DELETE FROM t_lisa_result WHERE moran_id IN (
                SELECT id FROM t_moran_result WHERE analysis_year=%s AND analysis_month=%s
            )""", (year, month))
        cur.execute("DELETE FROM t_moran_result WHERE analysis_year=%s AND analysis_month=%s", (year, month))

        # 插入全局结果
        cur.execute("""
            INSERT INTO t_moran_result (analysis_year, analysis_month, global_moran_i, p_value, z_score)
            VALUES (%s, %s, %s, %s, %s) RETURNING id
        """, (year, month, global_I, p_value, z_score))
        moran_id = cur.fetchone()[0]

        # 批量插入 LISA 结果
        values = []
        for i, code in enumerate(city_codes):
            values.append((moran_id, code, round(float(local_I_arr[i]), 6),
                           round(float(p_values_arr[i]), 6), cluster_types[i]))

        cur.executemany("""
            INSERT INTO t_lisa_result (moran_id, city_code, local_i, p_value, cluster_type)
            VALUES (%s, %s, %s, %s, %s)
        """, values)

    conn.commit()

def run_moran(year, month):
    conn = get_connection()
    print(f"[*] 正在计算 {year}年{month}月 Moran's I ...")

    rows = fetch_city_prices(conn, year, month)
    if len(rows) < 10:
        print(f"  [!] 数据不足 ({len(rows)} 个城市), 跳过")
        conn.close()
        return

    city_codes = [r[0] for r in rows]
    lons = np.array([float(r[1]) for r in rows])
    lats = np.array([float(r[2]) for r in rows])
    prices = np.array([float(r[3]) for r in rows])

    print(f"  城市数: {len(city_codes)}")

    # 构建权重矩阵
    W = build_distance_weights(lons, lats, k=8)

    # 全局 Moran's I
    global_I, z_score, p_value = compute_global_moran(prices, W)
    print(f"  Global Moran's I = {global_I}, Z = {z_score}, P = {p_value}")

    # 局部 LISA
    local_I_arr, p_values_arr, cluster_types = compute_lisa(prices, W)
    from collections import Counter
    ct = Counter(cluster_types)
    print(f"  LISA 分布: {dict(ct)}")

    # 写入数据库
    save_results(conn, year, month, global_I, z_score, p_value,
                 city_codes, local_I_arr, p_values_arr, cluster_types)
    print(f"  [OK] 已写入数据库 (moran_id 已生成)")
    conn.close()

if __name__ == '__main__':
    # 默认计算几个典型时间点
    time_points = [
        (2015, 1), (2015, 6), (2016, 1), (2016, 6),
        (2017, 1), (2017, 6), (2018, 1), (2018, 6),
        (2019, 1), (2019, 6), (2020, 1), (2020, 6),
        (2021, 1), (2021, 6), (2022, 1), (2022, 6),
        (2023, 1), (2023, 6), (2024, 1), (2024, 6),
        (2025, 1), (2025, 6),
    ]

    if len(sys.argv) == 3:
        year, month = int(sys.argv[1]), int(sys.argv[2])
        run_moran(year, month)
    else:
        print("=== 批量计算 Moran's I ===")
        for y, m in time_points:
            run_moran(y, m)
        print("\n=== 全部完成 ===")
