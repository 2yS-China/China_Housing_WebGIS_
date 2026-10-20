# Preparation and verification

This note describes checks completed while preparing the supplied project for a GitHub upload. It is not a claim of full system or statistical validation.

## Completed checks

| Check | Result |
| --- | --- |
| Workbook audit | 42,522 monthly rows, 4,119 annual rows, 626 area codes and 31 province-level names confirmed |
| Frontend clean dependency install | `npm ci --ignore-scripts --no-audit --no-fund` completed with the supplied lockfile |
| Frontend production build | `npm run build` passed; Vite 5.4.21 transformed 2,253 modules |
| Python syntax | All six Python source files parsed successfully |
| Backend XML | Maven POM and seven MyBatis XML mapping files parsed successfully |
| JSON | Package manifest, dependency lockfile and recorded data audit parsed successfully |
| Original application source preservation | 90 frontend, Java and MyBatis mapping files match the supplied project byte-for-byte |
| Original workbook preservation | Byte-for-byte identical |
| Original database snapshot preservation | Byte-for-byte identical, renamed from `.sql` to `.dump` |
| Frontend lockfile preservation | Byte-for-byte identical |
| Personal map key | Original key absent from prepared source and documentation |

The frontend build reported existing Sass legacy-API deprecation messages and large JavaScript bundle warnings; these did not prevent the build. Build outputs and installed dependencies are excluded from the upload package.

## Preparation changes

- Added English and Chinese README files, data/method notes, API reference, Windows setup instructions and the workbook audit utility.
- Added `.gitignore`, `.gitattributes`, Python dependency requirements and configuration examples.
- Replaced hard-coded Python connection dictionaries with a shared environment-based configuration module. The four original data/analysis scripts retain their existing processing and mathematical logic.
- Replaced hard-coded Spring datasource values with environment-variable placeholders.
- Omitted the original frontend `.env` and registry-specific `.npmrc`; the supplied personal map key is not distributed.
- Extracted an empty nine-table application schema from the supplied database archive metadata. Preserved the original custom-format snapshot separately.
- Excluded dependency directories, compiled Java output, Python bytecode, and generated frontend output.

## Not verified in this preparation

- Maven compilation and Java-backend execution against a live PostgreSQL/PostGIS database.
- Executing the extracted schema, workbook import or binary snapshot restore against a database.
- Full browser interaction with live API responses and authenticated Tianditu tiles.
- Independent numerical validation of global Moran inference, local LISA significance or K-means robustness.

See [Data and methods](DATA_AND_METHODS.md) for known coordinate, temporal-alignment and statistical limitations. The prepared repository provides source and reproducibility instructions; it is not presented as a fully tested deployment.
