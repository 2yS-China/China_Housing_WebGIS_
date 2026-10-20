# China Housing WebGIS

A full-stack WebGIS prototype for exploring second-hand housing prices across Chinese cities and districts. It connects Excel ingestion, PostgreSQL/PostGIS storage, Python analysis, Spring Boot APIs, and a Vue 3 map dashboard.

[中文说明](README.zh-CN.md) · [Data and methods](docs/DATA_AND_METHODS.md) · [API reference](docs/API.md) · [Windows setup](docs/SETUP_WINDOWS.md)

## Dataset at a glance

These counts were verified from the included workbook, rather than inferred from its filename.

| Item | Verified value |
| --- | ---: |
| Monthly price observations | 42,522 |
| Annual price summaries | 4,119 |
| Distinct administrative area codes | 626 |
| Distinct province-level names | 31 |
| Years represented | 2010–2025 |
| Price unit | CNY/m² |

The 626 codes represent areas at different administrative levels, including cities and districts. Coverage varies by area and date; this is not a complete 626-area monthly panel. The original workbook has one December 2025 row even though its filename ends in `2025.7`; the dashboard time slider ends in July 2025. See the [coverage notes](docs/DATA_AND_METHODS.md).

## Features

- Price-coloured point maps, proportional bubbles, province summaries, month-on-month changes, annual-average year-on-year changes, and an exploratory LISA layer.
- Time selection and playback, city search and monthly trends, and comparison of up to five selected areas.
- Price rankings, custom-period change rankings, province statistics, and ten configured city-group definitions.
- Province/price filtering and Excel export.
- Python K-means analysis of standardised price/longitude/latitude features, with presets for `k=3,4,5`.
- Global Moran's I and a simplified local Moran/LISA implementation using row-standardised eight-nearest-neighbour weights.

The map layer called `choropleth` in the code uses coloured point markers, not administrative-boundary polygons. The statistical modules are exploratory; limitations are documented below and in [Data and methods](docs/DATA_AND_METHODS.md).

## Technology

| Layer | Components |
| --- | --- |
| Frontend | Vue 3, Vite, Pinia, Element Plus, Leaflet, ECharts |
| Backend | Java 17, Spring Boot 3.2.5, MyBatis-Plus, Apache POI |
| Storage | PostgreSQL with PostGIS |
| Import and analysis | Python, openpyxl, psycopg2, NumPy, SciPy, scikit-learn |

Python scripts write imported data and analysis results to PostgreSQL. The Java backend reads these tables through REST APIs; the browser renders the results. The web API serves stored analysis results and does not run Python jobs on demand.

## Run locally

Prerequisites: Python 3.10+, Java 17, Maven 3.6.3+, a Node.js version compatible with Vite 5 (for example Node 20), and a running PostgreSQL instance with PostGIS installed. The commands below use Bash; [PowerShell equivalents](docs/SETUP_WINDOWS.md) are provided.

### 1. Configure the database connection

Run from the repository root. Use your own local database password.

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt

export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=china_housing
export DB_USER=postgres
export DB_PASSWORD='your_local_password'
```

The root `.env.example` documents the names. Python and Spring Boot read the process environment; they do **not** automatically load that example file. Set the same variables in each terminal used for these processes.

### 2. Create a fresh database and import the workbook

Use a new, empty database. The schema script creates the PostGIS extension, so the database account needs permission to create that extension, or an administrator must create it first.

```bash
PGPASSWORD="$DB_PASSWORD" createdb -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" "$DB_NAME"
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -v ON_ERROR_STOP=1 -f database/schema.sql
python import_data.py
```

On a fresh import, the workbook provides 626 area records, 31 province records, 42,522 monthly records and 4,119 annual records. Existing rows with the same unique keys are skipped by the importer; importing again does not update their prices or coordinates.

The original file named `china_housing.sql` was actually a PostgreSQL custom-format archive. It is preserved as `database/china_housing.dump`. For an alternative restore into a **different empty database**, use a PostgreSQL 18 `pg_restore` client and an installed PostGIS extension:

```bash
PGPASSWORD="$DB_PASSWORD" pg_restore -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" --no-owner --no-privileges --exit-on-error database/china_housing.dump
```

Choose either the workbook/schema route or the snapshot route for a given empty database; do not run both schema creation methods. The snapshot was produced by PostgreSQL 18.3. Workbook counts above do not assert that the snapshot has identical contents.

### 3. Populate optional analysis tables

```bash
python init_city_groups.py
python compute_cluster.py
python compute_moran.py 2024 6
# Optional: generate the script's full preset list of Moran/LISA time points.
python compute_moran.py
```

`init_city_groups.py` clears and rebuilds the two city-group tables. Group membership is a manually configured demonstration list, not an official urban-agglomeration definition. Clustering and Moran scripts replace results for the selected tasks/time points. Only dates with precomputed results can display those analysis layers.

### 4. Start the backend

In a terminal with the database environment variables set:

```bash
cd backend
mvn spring-boot:run
```

The API listens on `http://localhost:8081/api`. A simple data check is:

```bash
curl 'http://localhost:8081/api/map/bubble?year=2024&month=6'
```

### 5. Start the frontend

In another terminal:

```bash
cd frontend
cp .env.example .env.local
# Edit .env.local and set VITE_TIANDITU_KEY to your own browser map key.
npm ci
npm run dev
```

Open `http://localhost:5173`. Vite proxies `/api` requests to port 8081. A Tianditu browser key is needed for the current basemap; no personal key is included. `VITE_` values are exposed to the browser. The existing basemap URLs use HTTP, so an HTTPS deployment needs matching HTTPS-compatible tile URLs.

`npm run build` produces the frontend build. A built frontend needs a web server that forwards `/api` to the Java backend; the Vite development proxy is not included in static build output.

## Repository layout

| Path | Purpose |
| --- | --- |
| `frontend/` | Vue dashboard and frontend dependency lockfile |
| `backend/` | Java source, MyBatis SQL mappings and Maven configuration |
| `data/` | Supplied workbook and its data notes |
| `database/schema.sql` | Empty application schema extracted from the supplied archive |
| `database/china_housing.dump` | Supplied PostgreSQL custom-format snapshot |
| `import_data.py` | Workbook ingestion and coordinate assignment |
| `compute_cluster.py` | Price/location K-means presets |
| `compute_moran.py` | Global and simplified local Moran calculations |
| `init_city_groups.py` | Demonstration city-group definitions |
| `housing_config.py` | Shared environment-based Python connection settings |
| `scripts/audit_dataset.py` | Reproduce workbook counts and coordinate-assignment counts |
| `docs/` | Methods, API, setup and verification notes |

## Interpretation and current limitations

- Of the 626 imported area codes, 320 match the script's named coordinate lookup and 306 fall back to their province's representative coordinate. These are not 626 independently geocoded locations; fallback points can overlap and affect spatial results.
- Neighbours are selected using Euclidean distance in longitude/latitude degrees. K-means uses standardised coordinates with double weight on the price feature; it does not enforce spatial contiguity.
- Local Moran p-values use a fixed approximate variance, not permutation inference. LISA significance labels are a prototype output and need statistical validation. Global p-values also use an analytic approximation.
- The comparison chart uses the first area's date axis and assumes aligned series; uneven date coverage can misalign comparisons. The filter currently uses the selected end month, rather than the full start/end interval, and reports a count without redrawing the map.
- Dataset provider, collection methodology and redistribution terms are not recorded in the supplied project. No official-statistics provenance or transaction-level interpretation is claimed.

See [verification notes](docs/VERIFICATION.md) for the checks completed on this prepared repository.
