# API reference

Default base URL: `http://localhost:8081/api`. Most endpoints return `{ "code": 200, "msg": "...", "data": ... }`; failures may be represented by the response body rather than an HTTP error status. The Excel export returns a binary workbook.

| Method | Path | Parameters | Purpose |
| --- | --- | --- | --- |
| GET | `/city/search` | `keyword` | Area name/code search, up to 20 matches |
| GET | `/city/{cityCode}` | Path code | Area reference record |
| GET | `/city/{cityCode}/trend` | Path code | Available monthly observations |
| GET | `/city/compare` | `codes=110000,310000` | Trend series for selected codes |
| POST | `/city/filter` | JSON filter | Matching area-price rows |
| GET | `/map/choropleth` | `year`, `month` | Prices and point locations |
| GET | `/map/bubble` | `year`, `month` | Same underlying point/price dataset |
| GET | `/map/province` | `year`, `month` | Province-level summary values |
| GET | `/map/change/mom` | `year`, `month` | Month-on-month changes |
| GET | `/map/change/yoy` | `year` | Annual-average year-on-year changes |
| GET | `/rank/price` | `year`, `month`, `order=desc`, `limit=20` | Price ranking |
| GET | `/rank/change` | `startYear`, `startMonth`, `endYear`, `endMonth`, `order=desc`, `limit=20` | Two-date change ranking |
| GET | `/province/list` | None | Province reference records |
| GET | `/province/stats` | `provinceName`, `year`, `month` | Statistics for one province |
| GET | `/citygroup/list` | None | Stored demonstration groups |
| GET | `/citygroup/stats` | `year`, `month`, optional `groupId` | Available member price summaries |
| GET | `/analysis/moran` | `year`, optional `month` | Stored global/local Moran result |
| GET | `/analysis/cluster/tasks` | None | Stored K-means task names |
| GET | `/analysis/cluster` | `taskName` | Area assignments for one task |
| POST | `/export/excel` | JSON filter | Download matching rows as `.xlsx` |

Example filter body:

```json
{
  "provinces": ["北京市", "上海市"],
  "minPrice": 10000,
  "maxPrice": 100000,
  "endYear": 2024,
  "endMonth": 6
}
```

The DTO also has `startYear`/`startMonth`, but the current filter/export SQL does not apply them. The database must already contain analysis rows for analysis endpoints; they do not invoke Python. Current default CORS settings are for local prototyping and need review before hosting the backend publicly.
