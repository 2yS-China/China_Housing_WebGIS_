# Supplied workbook

`340城市房价历史数据（2010-2025.7）.xlsx` is preserved byte-for-byte from the uploaded project.

| Sheet | Required columns, in order | Data rows |
| --- | --- | ---: |
| 月度房价 | 行政区划代码, 城市, 所属省份, 年份, 月份, 二手房均价(元/㎡) | 42,522 |
| 年度房价 | 行政区划代码, 城市, 所属省份, 年份, 房价年均值(元/㎡), 房价年中位数(元/㎡), 年末房价(元/㎡) | 4,119 |

The importer uses the sheet names and column positions above. Keep six-digit area codes as strings when joining tables.

The workbook contains area-level aggregates, not individual housing transactions. Its original provider, collection methodology, citation and redistribution terms are not included in the source project. The annual sheet is imported as supplied, rather than regenerated from the monthly sheet. See [data and methods](../docs/DATA_AND_METHODS.md) for uneven temporal coverage and coordinate limitations.

Reproduce basic counts with `python scripts/audit_dataset.py` from the repository root.
