# 中国城市二手房 WebGIS

一个面向中国城市及区县二手房价格探索的全栈 WebGIS 原型，连接 Excel 数据导入、PostgreSQL/PostGIS 存储、Python 空间分析、Spring Boot API 与 Vue 3 交互地图。

[English README](README.md) · [数据与方法](docs/DATA_AND_METHODS.md) · [API](docs/API.md) · [Windows 启动说明](docs/SETUP_WINDOWS.md)

## 经工作簿核对的数据规模

| 项目 | 数量 |
| --- | ---: |
| 月度房价记录 | 42,522 |
| 年度房价汇总记录 | 4,119 |
| 不重复行政地区编码 | 626 |
| 省级地区名称 | 31 |
| 涉及年份 | 2010—2025 |
| 价格单位 | 元/平方米 |

626 个编码包含不同层级的城市、区县等地区，不能统一写成“626 个地级市”。各地区的时间覆盖不同，不是完整的平衡面板。工作簿虽然以 `2025.7` 命名，实际另有一条 2025 年 12 月记录；当前时间滑块截至 2025 年 7 月。

## 实现内容

- 房价着色点图、气泡图、省级汇总、月度环比、年度均价同比，以及探索性 LISA 地图。
- 时间选择与播放、城市搜索、单地区趋势及最多五个地区的趋势比较。
- 房价排名、自定义两期涨跌排名、省级统计与十组预设城市群配置。
- 按省份、房价、结束年月筛选，并导出 Excel。
- 对标准化后的房价、经度和纬度运行 K-means，预设 `k=3、4、5`。
- 使用八近邻行标准化权重计算全局 Moran's I 与简化的局部 Moran/LISA，并将结果写入数据库供前端读取。

代码中名为 `choropleth` 的图层实际采用点标记，没有实现行政区面状分级设色。分析由 Python 脚本预计算，Web API 读取已保存结果。

## 启动方式

需要 Python 3.10+、Java 17、Maven、兼容 Vite 5 的 Node.js（例如 Node 20），以及已安装 PostGIS 的 PostgreSQL。完整 Bash 命令见 [英文 README](README.md#run-locally)，Windows 用户见 [PowerShell 步骤](docs/SETUP_WINDOWS.md)。

1. 安装 `requirements.txt` 中的 Python 依赖。
2. 在终端设置 `DB_HOST`、`DB_PORT`、`DB_NAME`、`DB_USER`、`DB_PASSWORD`。根目录 `.env.example` 是变量说明，不会自动加载。
3. 创建空数据库，执行 `database/schema.sql`，再执行 `python import_data.py`。
4. 按需执行 `init_city_groups.py`、`compute_cluster.py`、`compute_moran.py`，生成城市群和分析结果。
5. 在 `backend/` 执行 `mvn spring-boot:run`，默认端口 8081。
6. 在 `frontend/` 将 `.env.example` 复制为 `.env.local`，填写自己的天地图浏览器 Key，执行 `npm ci` 和 `npm run dev`，打开 `http://localhost:5173`。

前端通过 Vite 将 `/api` 转发给后端。项目未保留原始个人地图 Key，数据库密码改为从环境变量读取。

原包的 `china_housing.sql` 实际是 PostgreSQL 18.3 导出的二进制备份，整理后保存在 `database/china_housing.dump`；应使用 `pg_restore`，不能按普通 SQL 文本执行。重建数据库与恢复备份是两条替代路线，不要向同一个库重复执行两种建表方式。

## 数据与统计边界

- 导入脚本中 320 个地区编码匹配到预设名称坐标，306 个使用省级代表点替代，会出现点重叠，也会影响空间邻接关系。
- 近邻按经纬度的欧氏距离计算；K-means 将房价标准化特征乘以 2，不保证空间连续。
- LISA 的 p 值采用固定近似方差，没有做置换检验，因此显著性分类属于待验证的原型输出。全局 p 值也采用解析近似。
- 多地区趋势比较默认日期序列对齐；数据覆盖不一致时会出现比较偏差。筛选接口目前按结束年月选取数据，开始年月不参与 SQL 筛选；筛选面板显示结果数，不会重新绘制地图。
- 城市群是脚本中的手动演示分组，成员不完整，不代表官方划定范围。
- 原始数据的提供方、采集口径与再分发条款未在项目中记录，不能据此声称为官方统计或实际成交记录。

## 文件整理范围

业务逻辑及分析公式沿用原项目；整理版增加中英文 README、数据与方法说明、API 文档、Windows 启动说明、依赖清单、配置示例和忽略文件。移除了 `node_modules`、`target`、Python 缓存及个人地图 Key，保留原始工作簿和数据库备份。

实际完成的验证见 [VERIFICATION.md](docs/VERIFICATION.md)。
