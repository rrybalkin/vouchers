-- indexes on table VISITORS
CREATE INDEX xif04_v ON visitors (association);

-- indexes on table VISITOR_TALON
CREATE INDEX xif05_tv ON visitor_talon (visitor_id);
CREATE INDEX xif06_tv ON visitor_talon (talon_id);