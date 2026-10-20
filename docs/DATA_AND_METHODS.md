# Data and methods

## Scope of the verified counts

The included workbook has 42,522 monthly observations and 4,119 annual summaries, with 626 distinct area codes and 31 province-level names. Counts refer to workbook rows with a nonempty area code. There are no duplicate monthly `(area code, year, month)` keys; all 42,522 monthly prices are present and positive.

There are 188 distinct observed year/month combinations from January 2010 to December 2025. This does not mean continuous coverage through December 2025. January–July 2025 each have 361 rows; December 2025 has one row; August–November 2025 are absent. The dashboard intentionally retains the original July 2025 upper bound. The number of areas represented in a month ranges from 1 to 611. The filename's reference to 340 cities is not used as an audited count.

`scripts/audit_dataset.py` reproduces row, code, time and coordinate-assignment counts. Its machine-readable output is recorded in `data-audit.json`.

## Import pipeline

`import_data.py` reads the two named Excel sheets, pads area codes to six characters, extracts area/province reference tables and batch-inserts price records. Monthly inserts use batches of 5,000. Unique keys support `ON CONFLICT ... DO NOTHING`; reruns skip existing records and are not an update mechanism.

The data model has nine application tables: `t_city`, `t_province`, `t_monthly_price`, `t_yearly_price`, `t_city_group`, `t_city_group_member`, `t_moran_result`, `t_lisa_result` and `t_spatial_cluster`.

`database/schema.sql` was extracted from the readable schema metadata in the supplied custom-format PostgreSQL archive. It preserves the nine application table definitions, sequences, defaults, constraints and indexes. Temporary staging tables from the snapshot are not needed for the workbook import and are omitted. The original archive is included separately without modification, under a `.dump` extension.

## Coordinates

The importer's `CITY_COORDS` dictionary is matched by area name. For unmatched names, it uses `PROVINCE_COORDS` as a fallback. Applied to the workbook's unique codes, this gives:

| Assignment path | Area codes |
| --- | ---: |
| Named coordinate lookup | 320 |
| Province representative-point fallback | 306 |
| No coordinate | 0 |

The lookup coordinates are not independently checked in this preparation. Area names are not globally unique, and the fallback produces overlapping points. PostGIS stores points with SRID 4326; this is a storage declaration, not a validation of location accuracy. Reliable spatial inference would require improved area matching and representative coordinates or boundaries.

## Price/location K-means

`compute_cluster.py` selects a month's positive-price observations with coordinates. It standardises three features—price, longitude and latitude—with `StandardScaler`, then multiplies the standardised price feature by 2. `KMeans` uses `random_state=42`, `n_init=10`, and task presets for 3, 4 or 5 clusters. Labels are reordered by cluster mean price before writing results to `t_spatial_cluster`.

This clusters price and position jointly. It does not optimise a geographic-distance metric, impose contiguity, select an optimal `k`, or validate stability/silhouette scores. The preset runner defines 11 tasks; this is a configured task count, not evidence that all tasks have been run successfully on every database snapshot.

## Global Moran's I

`compute_moran.py` creates an eight-nearest-neighbour matrix from Euclidean longitude/latitude distances and row-standardises it. For centred prices `z`, it computes `I = (n / sum(W)) * (z.T @ W @ z) / (z.T @ z)`.

The script includes an analytic variance and normal-tail p-value calculation. Those inferential outputs have not been independently validated against an established spatial-statistics implementation. Degree-based distances, tied/duplicated coordinates, and fallback locations influence neighbour selection. No permutation test or multiple-testing correction is implemented.

## Local Moran/LISA prototype

Local values use `local_I[i] = (z[i] / mean(z**2)) * sum(W[i,j] * z[j])`. The current p-value implementation sets the variance to a fixed `0.01` and uses a normal approximation. That is a simplified placeholder for inference, not a validated local significance test. Labels `HH`, `LL`, `HL`, `LH` and `NS` are displayed by the frontend, but must not be reported as confirmed significant spatial clusters.

The statistical formulae are retained as supplied. Preparing this repository does not mean that a methodological correction or validation study has been completed.

## Dashboard semantics

- `choropleth`: price-coloured point markers. No polygon choropleth is currently rendered.
- `bubble`: price-scaled point markers.
- `province`: summaries of available area-level observations. Means are not population- or transaction-weighted estimates.
- `mom`: change against the preceding calendar month where a match exists.
- `yoy`: change in annual average price against the preceding year, independent of the selected month.
- `moran`: stored local analysis results for a precomputed year/month.
- City comparison: the frontend limits selection to five areas. It currently uses the first series' dates as the x-axis and does not reindex other series onto a shared calendar.
- Filtering/export: SQL uses province, price bounds and the end year/month. Start dates are accepted by the DTO but are not applied by the current filter SQL. The filter panel shows a count; it does not replace the map layer.
- City groups: ten hard-coded demonstration definitions, with members inserted only when their names match the loaded database. These lists are incomplete and should not be presented as authoritative geographic definitions.

## Provenance

The uploaded project does not identify the workbook provider, collection methodology or redistribution terms. No official-statistics, actual-transaction or independent data-collection claim is made. The supplied annual aggregates and date labels are preserved rather than silently corrected.
