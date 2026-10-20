"""
初始化城市群数据: t_city_group + t_city_group_member
"""
import psycopg2

from housing_config import DB_CONFIG as DB

GROUPS = {
    '长三角城市群': [
        '上海', '南京', '杭州', '苏州', '无锡', '常州', '镇江', '扬州', '泰州', '南通',
        '宁波', '温州', '嘉兴', '湖州', '绍兴', '合肥', '芜湖', '马鞍山', '滁州',
    ],
    '珠三角城市群': [
        '广州', '深圳', '佛山', '东莞', '珠海', '中山', '惠州', '肇庆', '江门',
    ],
    '京津冀城市群': [
        '北京', '天津', '石家庄', '保定', '唐山', '廊坊', '秦皇岛',
    ],
    '长江中游城市群': [
        '武汉', '长沙', '南昌', '宜昌', '襄阳', '岳阳', '株洲', '湘潭', '九江',
    ],
    '成渝城市群': [
        '重庆',  # 成都不在数据库
    ],
    '关中平原城市群': [
        '西安', '咸阳', '宝鸡', '渭南',
    ],
    '山东半岛城市群': [
        '济南', '青岛', '烟台', '潍坊', '淄博',
    ],
    '海峡西岸城市群': [
        '福州', '厦门', '泉州', '漳州', '莆田',
    ],
    '哈长城市群': [
        '哈尔滨', '长春', '大连', '沈阳',
    ],
    '北部湾城市群': [
        '南宁', '海口', '三亚',
    ],
}

def main():
    conn = psycopg2.connect(**DB)
    cur = conn.cursor()

    # 清理旧数据
    cur.execute('DELETE FROM t_city_group_member')
    cur.execute('DELETE FROM t_city_group')
    conn.commit()

    # 构建城市名->code映射
    cur.execute('SELECT city_code, city_name FROM t_city')
    name2code = {r[1]: r[0] for r in cur.fetchall()}

    total_groups = 0
    total_members = 0

    for group_name, city_names in GROUPS.items():
        cur.execute(
            'INSERT INTO t_city_group (group_name, description) VALUES (%s, %s) RETURNING group_id',
            (group_name, f'{group_name}核心城市')
        )
        gid = cur.fetchone()[0]
        total_groups += 1

        for cname in city_names:
            code = name2code.get(cname)
            if not code:
                print(f'  [SKIP] {cname} 不在数据库中')
                continue
            cur.execute(
                'INSERT INTO t_city_group_member (group_id, city_code) VALUES (%s, %s)',
                (gid, code)
            )
            total_members += 1

        print(f'[OK] {group_name}: {len([c for c in city_names if name2code.get(c)])} 个城市')

    conn.commit()
    cur.close()
    conn.close()
    print(f'\n=== 完成: {total_groups} 个城市群, {total_members} 个成员 ===')

if __name__ == '__main__':
    main()
