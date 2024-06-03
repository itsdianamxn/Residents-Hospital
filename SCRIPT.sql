CREATE OR REPLACE PROCEDURE insert_hospital(
    p_name VARCHAR,
    p_capacity INT,
    p_grade VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO hospitals (name, capacity, grade) 
    VALUES (p_name, p_capacity, p_grade);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting hospital: %', SQLERRM;
END;
$$;
CREATE OR REPLACE PROCEDURE insert_resident(
    p_name VARCHAR,
    p_assigned BOOLEAN,
    p_grade VARCHAR,
    p_resident_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO residents (name, assigned, grade, resident_id)
    VALUES (p_name, p_assigned, p_grade, p_resident_id);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting resident: %', SQLERRM;
END;
$$;
-- Insert Hospital
CREATE OR REPLACE PROCEDURE insert_hospital(
    p_name VARCHAR,
    p_capacity INT,
    p_grade VARCHAR,
    p_hospital_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO hospitals (name, capacity, grade, hospital_id) 
    VALUES (p_name, p_capacity, p_grade, p_hospital_id);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting hospital: %', SQLERRM;
END;
$$;

-- Update Hospital
CREATE OR REPLACE PROCEDURE update_hospital(
    p_name VARCHAR,
    p_capacity INT,
    p_grade VARCHAR,
    p_hospital_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE hospitals
    SET name = p_name,
        capacity = p_capacity,
        grade = p_grade
    WHERE hospital_id = p_hospital_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error updating hospital: %', SQLERRM;
END;
$$;

-- Delete Hospital
CREATE OR REPLACE PROCEDURE delete_hospital(p_hospital_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM hospitals WHERE hospital_id = p_hospital_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error deleting hospital: %', SQLERRM;
END;
$$;

-- Get Hospital
CREATE OR REPLACE FUNCTION get_hospital(p_hospital_id INT)
RETURNS TABLE(name VARCHAR, capacity INT, grade VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT name, capacity, grade
    FROM hospitals
    WHERE hospital_id = p_hospital_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error fetching hospital: %', SQLERRM;
        RETURN QUERY SELECT NULL::VARCHAR, NULL::INT, NULL::VARCHAR;
END;
$$;

CREATE OR REPLACE PROCEDURE insert_resident(
    p_name VARCHAR,
    p_assigned BOOLEAN,
    p_grade VARCHAR,
    p_resident_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO residents (name, assigned, grade, resident_id)
    VALUES (p_name, p_assigned, p_grade, p_resident_id);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting resident: %', SQLERRM;
END;
$$;

CREATE OR REPLACE PROCEDURE update_resident(
    p_name VARCHAR,
    p_assigned BOOLEAN,
    p_grade VARCHAR,
    p_resident_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE residents
    SET name = p_name,
        assigned = p_assigned,
        grade = p_grade
    WHERE resident_id = p_resident_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error updating resident: %', SQLERRM;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_resident(p_resident_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM residents WHERE resident_id = p_resident_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error deleting resident: %', SQLERRM;
END;
$$;

CREATE OR REPLACE FUNCTION get_resident(p_resident_id INT)
RETURNS TABLE(name VARCHAR, assigned BOOLEAN, grade VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT name, assigned, grade
    FROM residents
    WHERE resident_id = p_resident_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error fetching resident: %', SQLERRM;
        RETURN QUERY SELECT NULL::VARCHAR, NULL::BOOLEAN, NULL::VARCHAR;
END;
$$;

CREATE OR REPLACE PROCEDURE insert_hospital_specialization(
    p_hospital_id INT,
    p_specialization_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO hospital_specialization (hospital_id, specialization_id)
    VALUES (p_hospital_id, p_specialization_id);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting hospital specialization: %', SQLERRM;
END;
$$;

CREATE OR REPLACE FUNCTION get_specializations_by_hospital(p_hospital_id INT)
RETURNS TABLE(specialization_id INT, name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT s.specialization_id, s.name
    FROM specializations s
    JOIN hospital_specialization hs ON s.specialization_id = hs.specialization_id
    WHERE hs.hospital_id = p_hospital_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error fetching specializations: %', SQLERRM;
        RETURN QUERY SELECT NULL::INT, NULL::VARCHAR;
END;
$$;


CREATE OR REPLACE PROCEDURE insert_residents_specialization(
    p_resident_id INT,
    p_specialization_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO residents_specialization (resident_id, specialization_id)
    VALUES (p_resident_id, p_specialization_id);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting residents specialization: %', SQLERRM;
END;
$$;


CREATE OR REPLACE PROCEDURE delete_residents_specialization(
    p_resident_id INT,
    p_specialization_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM residents_specialization
    WHERE resident_id = p_resident_id
      AND specialization_id = p_specialization_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error deleting residents specialization: %', SQLERRM;
END;
$$;

CREATE OR REPLACE FUNCTION get_specializations_by_resident(p_resident_id INT)
RETURNS TABLE(specialization_id INT, name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT s.specialization_id, s.name
    FROM specializations s
    JOIN residents_specialization rs ON s.specialization_id = rs.specialization_id
    WHERE rs.resident_id = p_resident_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error fetching specializations: %', SQLERRM;
        RETURN QUERY SELECT NULL::INT, NULL::VARCHAR;
END;
$$;


CREATE OR REPLACE PROCEDURE insert_specialization(
    p_specialization_id INT,
    p_name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO specializations (specialization_id, name)
    VALUES (p_specialization_id, p_name);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting specialization: %', SQLERRM;
END;
$$;


CREATE OR REPLACE PROCEDURE update_specialization(
    p_specialization_id INT,
    p_name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE specializations
    SET name = p_name
    WHERE specialization_id = p_specialization_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error updating specialization: %', SQLERRM;
END;
$$;


CREATE OR REPLACE PROCEDURE delete_specialization(p_specialization_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM specializations WHERE specialization_id = p_specialization_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error deleting specialization: %', SQLERRM;
END;
$$;

CREATE OR REPLACE FUNCTION get_specialization(p_specialization_id INT)
RETURNS TABLE(name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT name
    FROM specializations
    WHERE specialization_id = p_specialization_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error fetching specialization: %', SQLERRM;
        RETURN QUERY SELECT NULL::VARCHAR;
END;
$$;

CREATE OR REPLACE PROCEDURE insert_matching(
    p_hospital_id INT,
    p_resident_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO matchings (hospital_id, resident_id)
    VALUES (p_hospital_id, p_resident_id);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inserting matching: %', SQLERRM;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_matching(
    p_hospital_id INT,
    p_resident_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM matchings
    WHERE hospital_id = p_hospital_id
      AND resident_id = p_resident_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error deleting matching: %', SQLERRM;
END;
$$;

CREATE OR REPLACE FUNCTION get_matchings_by_hospital(p_hospital_id INT)
RETURNS TABLE(hospital_id INT, resident_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT hospital_id, resident_id
    FROM matchings
    WHERE hospital_id = p_hospital_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error fetching matchings by hospital: %', SQLERRM;
        RETURN QUERY SELECT NULL::INT, NULL::INT;
END;
$$;
CREATE OR REPLACE FUNCTION get_matchings_by_resident(p_resident_id INT)
RETURNS TABLE(hospital_id INT, resident_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT hospital_id, resident_id
    FROM matchings
    WHERE resident_id = p_resident_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Error fetching matchings by resident: %', SQLERRM;
        RETURN QUERY SELECT NULL::INT, NULL::INT;
END;
$$;
select * from hospital_specialization where hospital_id = 262;
select * from residents_specialization where resident_id = 500;

CREATE OR REPLACE PROCEDURE make_preferences()
LANGUAGE plpgsql
AS $$
	DECLARE res_id INT;
BEGIN
	FOR res_id IN SELECT resident_id FROM residents
	LOOP
		CALL make_preferences_of_resident(res_id);
	END LOOP;
	
END;
$$;
select * from specializations;
select * from matchings;

call make_preferences();
	
Create table resident_hospital_preferences (resident_id INT, hospital_id INT);
delete from resident_hospital_preferences;

select * from resident_hospital_preferences where hospital_id = 261;

CALL make_preferences_of_resident(500);
--HR Instance
CREATE OR REPLACE PROCEDURE make_preferences_of_resident(p_resident_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Clear existing preferences
    DELETE FROM resident_hospital_preferences WHERE resident_id = p_resident_id;
    
    -- Insert new preferences
    INSERT INTO resident_hospital_preferences (resident_id, hospital_id)
    SELECT r.resident_id, h.hospital_id
    FROM residents r
    JOIN residents_specialization rs ON r.resident_id = rs.resident_id
    JOIN hospital_specialization hs ON rs.specialization_id = hs.specialization_id
    JOIN hospitals h ON hs.hospital_id = h.hospital_id
    WHERE r.resident_id = p_resident_id ORDER BY h.grade DESC;
END;
$$;

CREATE OR REPLACE PROCEDURE make_preferences_of_hospital(p_hospital_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Clear existing preferences
    DELETE FROM hospital_resident_preferences WHERE hospital_id = p_hospital_id;

    -- Insert new preferences
    INSERT INTO hospital_resident_preferences (hospital_id, resident_id)
    SELECT h.hospital_id, r.resident_id
    FROM hospitals h
    JOIN hospital_specialization hs ON h.hospital_id = hs.hospital_id
    JOIN resident_specialization rs ON hs.specialization_id = rs.specialization_id
    JOIN residents r ON rs.resident_id = r.resident_id
    WHERE h.hospital_id = p_hospital_id;
END;
$$;

Select unnest(string_to_array('A, B,C D', ','));



CREATE OR REPLACE FUNCTION add_resident(
    p_name VARCHAR,
    p_grade INT,
	p_specializations VARCHAR
)
RETURNS integer
LANGUAGE plpgsql
AS $$
	DECLARE spec VARCHAR;
	DECLARE	spec_id INT;
	DECLARE	res_id INT;
	DECLARE	t VARCHAR;
BEGIN
    -- Insert resident
	INSERT into residents (name, assigned, grade) VALUES (p_name, false, p_grade)
			returning resident_id into res_id;
	FOREACH t in array string_to_array(p_specializations, ',')
		LOOP
			spec := TRIM(t);
			spec_id := (SELECT specialization_id from specializations WHERE name = spec limit 1);
			IF (spec_id is null) THEN 
				insert into specializations values (spec) returning specialization_id into spec_id;
			END IF;
			INSERT into residents_specialization values (res_id, spec_id);
		END LOOP;
	return res_id;
END;
$$;
CREATE OR REPLACE FUNCTION add_hospital(
    p_name VARCHAR,
    p_capacity INT,
    p_grade INT,
	p_specializations VARCHAR
)
RETURNS integer
LANGUAGE plpgsql
AS $$
	DECLARE spec VARCHAR;
	DECLARE	spec_id INT;
	DECLARE	h_id INT;
	DECLARE	t VARCHAR;
BEGIN
    -- Insert hospital
    INSERT INTO hospitals (name, capacity, grade)
    VALUES (p_name, p_capacity, p_grade) returning hospital_id into h_id;

FOREACH t in array string_to_array(p_specializations, ',')
		LOOP
			spec := TRIM(t);
			spec_id := (SELECT specialization_id from specializations WHERE name = spec limit 1);
			IF (spec_id is null) THEN 
				insert into specializations values (spec) returning specialization_id into spec_id;
			END IF;
			INSERT into hospital_specialization values (h_id, spec_id);
		END LOOP;
	return h_id;
END;
$$;
select * from specializations;
SELECT add_hospital('Diana ABC', 7, 6, 'Dermatology, Ophtalmology');

CREATE OR REPLACE FUNCTION get_unassigned_resident()
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    res_id INT;
BEGIN
    SELECT resident_id
    INTO res_id
    FROM residents
    WHERE NOT assigned
    ORDER BY grade DESC
    LIMIT 1;

    RETURN res_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_assigned_residents(p_hospital_id INT)
RETURNS TABLE(resident_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT resident_id
    FROM matchings
    WHERE hospital_id = p_hospital_id;
END;
$$;


CREATE OR REPLACE FUNCTION get_worst_resident(p_hospital_id INT)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    res_id INT;
BEGIN
    SELECT resident_id
    INTO res_id
    FROM matchings
    WHERE hospital_id = p_hospital_id
    ORDER BY (SELECT grade FROM residents WHERE resident_id = matchings.resident_id) ASC
    LIMIT 1;

    RETURN res_id;
END;
$$;


CREATE OR REPLACE PROCEDURE print_pairings()
LANGUAGE plpgsql
AS $$
DECLARE
    rec RECORD;
BEGIN
    FOR rec IN
        SELECT h.name AS hospital_name, r.name AS resident_name
        FROM matchings m
        JOIN hospitals h ON m.hospital_id = h.hospital_id
        JOIN residents r ON m.resident_id = r.resident_id
    LOOP
        RAISE NOTICE '% is paired with %', rec.resident_name, rec.hospital_name,;
    END LOOP;
END;
$$;

Call print_PAIRINGS();

CALL make_pairings();
CREATE OR REPLACE PROCEDURE make_pairings()
LANGUAGE plpgsql
AS $$
DECLARE
    ri INT;
    hj INT;
    rk INT;
    res RECORD;
    hos RECORD;
BEGIN
    -- Reset all residents to unassigned
	CALL CLEAR();
	CALL make_preferences();

    -- Main pairing loop
    ri := get_unassigned_resident();
    WHILE ri IS NOT NULL AND NOT are_hospitals_filled() LOOP
        SELECT hospital_id INTO hj
        FROM resident_hospital_preferences --natural join hospitals 
        WHERE resident_id = ri
        -- ORDER BY hospitals.grade
        LIMIT 1;

        WHILE hj IS NOT NULL AND (SELECT openPos FROM hospitals WHERE hospital_id = hj) = 0 LOOP
            DELETE FROM resident_hospital_preferences
            WHERE resident_id = ri AND hospital_id = hj;

            SELECT hospital_id INTO hj
            FROM resident_hospital_preferences --natural join hospitals 
            WHERE resident_id = ri
            --ORDER BY grade
            LIMIT 1;
        END LOOP;

        IF hj IS NULL THEN
            UPDATE residents SET assigned = TRUE WHERE resident_id = ri;
            ri := get_unassigned_resident();
            CONTINUE;
        END IF;

        INSERT INTO matchings (hospital_id, resident_id) VALUES (hj, ri);
        UPDATE residents SET assigned = TRUE WHERE resident_id = ri;
        UPDATE hospitals SET openPos = openPos - 1 WHERE hospital_id = hj;

        ri := get_unassigned_resident();
    END LOOP;

    -- Print pairings
    CALL print_pairings();
END;
$$;

CREATE OR REPLACE FUNCTION are_hospitals_filled()
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if there are any hospitals with capacity greater than 0
	PERFORM 1
	FROM hospitals
    WHERE openPos > 0;
	RETURN NOT FOUND;
END;
$$;


CREATE OR REPLACE PROCEDURE clear()
LANGUAGE plpgsql
AS $$
BEGIN
	DELETE from matchings;
	DELETE FROM resident_hospital_preferences;
	UPDATE residents set assigned = FALSE;
	UPDATE hospitals set openPos = capacity;

END;
$$;

SELECT * FROM hospitals;
CALL CLEAR();

select * from resident_hospital_preferences;
select are_hospitals_filled();


select * from hospitals;




ALTER TABLE specializations
ADD COLUMN specialization_id SERIAL PRIMARY KEY;