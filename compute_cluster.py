"""
空间聚类分析: 基于房价+经纬度的 K-Means 聚类，写入 t_spatial_cluster
依赖: pip install psycopg2 numpy scikit-learn
"""
import psycopg2
import numpy as np
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler

from housing_config import DB_CONFIG as DB

def run_cluster(year, month, k=5):
    conn = psycopg2.connect(**DB)
    cur = conn.cursor()

    # 获取有价格和坐标的城市
    cur.execute("""
        SELECT c.city_code, c.city_name, c.province_name, c.lon, c.lat, mp.price
        FROM t_monthly_price mp
        JOIN t_city c ON c.city_code = mp.city_code
        WHERE mp.year = %s AND mp.month = %s
          AND c.lon IS NOT NULL AND c.lat IS NOT NULL
          AND mp.price IS NOT NULL AND mp.price > 0
        ORDER BY c.city_code
    """, (year, month))
    rows = cur.fetchall()

    if len(rows) < k:
        print(f"[!] {year}年{month}月数据不足 ({len(rows)}), 跳过")
        conn.close()
        return

    city_codes = [r[0] for r in rows]
    prices = np.array([float(r[5]) for r in rows])
    lons = np.array([float(r[3]) for r in rows])
    lats = np.array([float(r[4]) for r in rows])

    # 特征: 价格(权重高) + 经纬度(空间位置)
    features = np.column_stack([prices, lons, lats])
    scaler = StandardScaler()
    features_scaled = scaler.fit_transform(features)
    # 加大价格权重
    features_scaled[:, 0] *= 2.0

    km = KMeans(n_clusters=k, random_state=42, n_init=10)
    labels = km.fit_predict(features_scaled)

    # 按聚类均价排序，重新编号 0=最低, k-1=最高
    cluster_avg = {}
    for i in range(k):
        mask = labels == i
        cluster_avg[i] = prices[mask].mean()
    sorted_clusters = sorted(cluster_avg.keys(), key=lambda x: cluster_avg[x])
    label_map = {old: new for new, old in enumerate(sorted_clusters)}
    new_labels = [label_map[l] for l in labels]

    # 聚类描述
    descs = {
        0: '低价梯队',
        1: '中低价梯队',
        2: '中等梯队',
        3: '中高价梯队',
        4: '高价梯队',
    }

    task_name = f'房价空间聚类_{year}年{month}月_K{k}'

    # 删除旧同名任务
    cur.execute("DELETE FROM t_spatial_cluster WHERE task_name = %s", (task_name,))

    # 插入
    values = []
    for i, code in enumerate(city_codes):
        values.append((task_name, k, code, new_labels[i], descs.get(new_labels[i], f'第{new_labels[i]}类')))

    cur.executemany("""
        INSERT INTO t_spatial_cluster (task_name, cluster_k, city_code, cluster_label, cluster_desc)
        VALUES (%s, %s, %s, %s, %s)
    """, values)
    conn.commit()

    # 打印统计
    from collections import Counter
    ct = Counter(new_labels)
    print(f"[OK] {task_name}: {len(city_codes)} 城市")
    for label in sorted(ct.keys()):
        avg = round(cluster_avg[sorted_clusters[label]], 0)
        print(f"  第{label}类 ({descs.get(label,'')}): {ct[label]}城市, 均价≈{avg}")

    cur.close()
    conn.close()

if __name__ == '__main__':
    # 生成几个典型时间点的聚类
    tasks = [
        (2020, 1, 5), (2021, 1, 5), (2022, 1, 5),
        (2023, 1, 5), (2024, 1, 5), (2025, 1, 5),
        (2023, 6, 5), (2024, 6, 5), (2025, 6, 5),
        (2025, 1, 3), (2025, 1, 4),  # 不同 K 值
    ]
    print("=== 批量空间聚类 ===")
    for y, m, k in tasks:
        run_cluster(y, m, k)
    print("\n=== 全部完成 ===")
