# Windows / PowerShell setup

Install Python 3.10+, Java 17, Maven, a Node.js version compatible with Vite 5, and PostgreSQL with PostGIS. Ensure `python`, `java`, `mvn`, `npm`, `createdb` and `psql` are available in your terminal. PostgreSQL's `bin` folder may need to be added to `PATH`.

From the extracted repository root:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt

$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "china_housing"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "your_local_password"
$env:PGPASSWORD = $env:DB_PASSWORD

createdb -h $env:DB_HOST -p $env:DB_PORT -U $env:DB_USER $env:DB_NAME
psql -h $env:DB_HOST -p $env:DB_PORT -U $env:DB_USER -d $env:DB_NAME -v ON_ERROR_STOP=1 -f database/schema.sql
python import_data.py

python init_city_groups.py
python compute_cluster.py
python compute_moran.py 2024 6
```

Use a new, empty database. If it already exists, omit `createdb`; only run the schema script when its tables do not yet exist. Do not combine this fresh-import route with the snapshot restore route.

Start the backend in the same terminal so it inherits the database variables:

```powershell
cd backend
mvn spring-boot:run
```

In a second PowerShell terminal, from the repository root:

```powershell
cd frontend
Copy-Item .env.example .env.local
notepad .env.local
# Fill VITE_TIANDITU_KEY with your own browser map key, then save.
npm ci
npm run dev
```

Open `http://localhost:5173`. If your PowerShell setup blocks activation scripts, you can invoke `.\.venv\Scripts\python.exe` directly instead of activating the environment. No machine-wide execution-policy change is required.

To audit the supplied workbook from the repository root:

```powershell
.\.venv\Scripts\python.exe scripts/audit_dataset.py
```

The root `.env.example` is a reference; Python and Spring Boot read environment variables set in the terminal. Vite reads `frontend/.env.local`. No database password or personal Tianditu key is supplied.
