-- Empty application schema, extracted from the supplied PostgreSQL archive.
-- For a NEW empty database only. Data are imported with import_data.py.

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;

CREATE TABLE public.t_city (
    city_code character varying(6) NOT NULL,
    city_name character varying(50) NOT NULL,
    province_name character varying(50) NOT NULL,
    lon numeric(10,6),
    lat numeric(10,6),
    geom_point public.geometry(Point,4326),
    geom_boundary public.geometry(MultiPolygon,4326)
);

CREATE TABLE public.t_city_group (
    group_id integer NOT NULL,
    group_name character varying(50) NOT NULL,
    description character varying(200)
);

CREATE TABLE public.t_city_group_member (
    id integer NOT NULL,
    group_id integer NOT NULL,
    city_code character varying(6) NOT NULL
);

CREATE TABLE public.t_lisa_result (
    id integer NOT NULL,
    moran_id integer NOT NULL,
    city_code character varying(6) NOT NULL,
    local_i numeric(10,6),
    p_value numeric(10,6),
    cluster_type character varying(10)
);

CREATE TABLE public.t_monthly_price (
    id integer NOT NULL,
    city_code character varying(6) NOT NULL,
    year smallint NOT NULL,
    month smallint NOT NULL,
    price numeric(10,2)
);

CREATE TABLE public.t_moran_result (
    id integer NOT NULL,
    analysis_year smallint NOT NULL,
    analysis_month smallint,
    global_moran_i numeric(10,6),
    p_value numeric(10,6),
    z_score numeric(10,6),
    created_at timestamp without time zone DEFAULT now()
);

CREATE TABLE public.t_province (
    province_code character varying(6) NOT NULL,
    province_name character varying(50) NOT NULL,
    geom public.geometry(MultiPolygon,4326),
    center_lon numeric(10,6),
    center_lat numeric(10,6)
);

CREATE TABLE public.t_spatial_cluster (
    id integer NOT NULL,
    task_name character varying(100) NOT NULL,
    cluster_k smallint NOT NULL,
    city_code character varying(6) NOT NULL,
    cluster_label smallint NOT NULL,
    cluster_desc character varying(50),
    created_at timestamp without time zone DEFAULT now()
);

CREATE TABLE public.t_yearly_price (
    id integer NOT NULL,
    city_code character varying(6) NOT NULL,
    year smallint NOT NULL,
    avg_price numeric(10,2),
    median_price numeric(10,2),
    year_end_price numeric(10,2)
);

CREATE SEQUENCE public.t_city_group_group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.t_city_group_member_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.t_lisa_result_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.t_monthly_price_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.t_moran_result_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.t_spatial_cluster_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.t_yearly_price_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY public.t_city_group ALTER COLUMN group_id SET DEFAULT nextval('public.t_city_group_group_id_seq'::regclass);

ALTER TABLE ONLY public.t_city_group_member ALTER COLUMN id SET DEFAULT nextval('public.t_city_group_member_id_seq'::regclass);

ALTER TABLE ONLY public.t_lisa_result ALTER COLUMN id SET DEFAULT nextval('public.t_lisa_result_id_seq'::regclass);

ALTER TABLE ONLY public.t_monthly_price ALTER COLUMN id SET DEFAULT nextval('public.t_monthly_price_id_seq'::regclass);

ALTER TABLE ONLY public.t_moran_result ALTER COLUMN id SET DEFAULT nextval('public.t_moran_result_id_seq'::regclass);

ALTER TABLE ONLY public.t_spatial_cluster ALTER COLUMN id SET DEFAULT nextval('public.t_spatial_cluster_id_seq'::regclass);

ALTER TABLE ONLY public.t_yearly_price ALTER COLUMN id SET DEFAULT nextval('public.t_yearly_price_id_seq'::regclass);

ALTER TABLE ONLY public.t_city_group
    ADD CONSTRAINT t_city_group_group_name_key UNIQUE (group_name);

ALTER TABLE ONLY public.t_city_group_member
    ADD CONSTRAINT t_city_group_member_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_city_group
    ADD CONSTRAINT t_city_group_pkey PRIMARY KEY (group_id);

ALTER TABLE ONLY public.t_city
    ADD CONSTRAINT t_city_pkey PRIMARY KEY (city_code);

ALTER TABLE ONLY public.t_lisa_result
    ADD CONSTRAINT t_lisa_result_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_monthly_price
    ADD CONSTRAINT t_monthly_price_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_moran_result
    ADD CONSTRAINT t_moran_result_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_province
    ADD CONSTRAINT t_province_pkey PRIMARY KEY (province_code);

ALTER TABLE ONLY public.t_province
    ADD CONSTRAINT t_province_province_name_key UNIQUE (province_name);

ALTER TABLE ONLY public.t_spatial_cluster
    ADD CONSTRAINT t_spatial_cluster_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_yearly_price
    ADD CONSTRAINT t_yearly_price_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.t_city_group_member
    ADD CONSTRAINT uk_group_city UNIQUE (group_id, city_code);

ALTER TABLE ONLY public.t_monthly_price
    ADD CONSTRAINT uk_monthly UNIQUE (city_code, year, month);

ALTER TABLE ONLY public.t_yearly_price
    ADD CONSTRAINT uk_yearly UNIQUE (city_code, year);

ALTER TABLE ONLY public.t_spatial_cluster
    ADD CONSTRAINT fk_cluster_city FOREIGN KEY (city_code) REFERENCES public.t_city(city_code);

ALTER TABLE ONLY public.t_city_group_member
    ADD CONSTRAINT fk_group FOREIGN KEY (group_id) REFERENCES public.t_city_group(group_id);

ALTER TABLE ONLY public.t_lisa_result
    ADD CONSTRAINT fk_lisa_city FOREIGN KEY (city_code) REFERENCES public.t_city(city_code);

ALTER TABLE ONLY public.t_lisa_result
    ADD CONSTRAINT fk_lisa_moran FOREIGN KEY (moran_id) REFERENCES public.t_moran_result(id);

ALTER TABLE ONLY public.t_city_group_member
    ADD CONSTRAINT fk_member_city FOREIGN KEY (city_code) REFERENCES public.t_city(city_code);

ALTER TABLE ONLY public.t_monthly_price
    ADD CONSTRAINT fk_monthly_city FOREIGN KEY (city_code) REFERENCES public.t_city(city_code);

ALTER TABLE ONLY public.t_yearly_price
    ADD CONSTRAINT fk_yearly_city FOREIGN KEY (city_code) REFERENCES public.t_city(city_code);

CREATE INDEX idx_city_geom_boundary ON public.t_city USING gist (geom_boundary);

CREATE INDEX idx_city_geom_point ON public.t_city USING gist (geom_point);

CREATE INDEX idx_city_province ON public.t_city USING btree (province_name);

CREATE INDEX idx_cluster_city ON public.t_spatial_cluster USING btree (city_code);

CREATE INDEX idx_cluster_task ON public.t_spatial_cluster USING btree (task_name);

CREATE INDEX idx_lisa_city ON public.t_lisa_result USING btree (city_code);

CREATE INDEX idx_lisa_moran ON public.t_lisa_result USING btree (moran_id);

CREATE INDEX idx_monthly_city ON public.t_monthly_price USING btree (city_code);

CREATE INDEX idx_monthly_price ON public.t_monthly_price USING btree (price);

CREATE INDEX idx_monthly_year_month ON public.t_monthly_price USING btree (year, month);

CREATE INDEX idx_province_geom ON public.t_province USING gist (geom);

CREATE INDEX idx_yearly_city ON public.t_yearly_price USING btree (city_code);

CREATE INDEX idx_yearly_year ON public.t_yearly_price USING btree (year);
