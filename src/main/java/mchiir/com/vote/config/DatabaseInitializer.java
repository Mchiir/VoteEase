package mchiir.com.vote.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        String sql = """
            CREATE OR REPLACE FUNCTION log_user_create() RETURNS trigger AS $$
            BEGIN
                INSERT INTO logs(user_id, action_type, details, timestamp)
                VALUES (
                    NEW.id,
                    'CREATING',
                    'Created user with username: ' || NEW.name || ', role: ' || NEW.role,
                    NOW()
                );
                RETURN NEW;
            END;
            $$ LANGUAGE plpgsql;

            CREATE OR REPLACE FUNCTION log_user_update() RETURNS trigger AS $$
            BEGIN
                INSERT INTO logs(user_id, action_type, details, timestamp)
                VALUES (
                    NEW.id,
                    'UPDATING',
                    'Updated user with role: ' || OLD.role || ', to new with role: ' || NEW.role,
                    NOW()
                );
                RETURN NEW;
            END;
            $$ LANGUAGE plpgsql;

            CREATE OR REPLACE FUNCTION log_user_delete() RETURNS trigger AS $$
            BEGIN
                INSERT INTO logs(user_id, action_type, details, timestamp)
                VALUES (
                    OLD.id,
                    'DELETING',
                    'Deleted user with username: ' || OLD.name || ', role: ' || OLD.role,
                    NOW()
                );
                RETURN OLD;
            END;
            $$ LANGUAGE plpgsql;

            CREATE TRIGGER trg_user_create
            AFTER INSERT ON users
            FOR EACH ROW
            EXECUTE FUNCTION log_user_create();

            CREATE TRIGGER trg_user_update
            AFTER UPDATE ON users
            FOR EACH ROW
            EXECUTE FUNCTION log_user_update();

            CREATE TRIGGER trg_user_delete
            BEFORE DELETE ON users
            FOR EACH ROW
            EXECUTE FUNCTION log_user_delete();
        """;

        // Execute the SQL script
        jdbcTemplate.execute(sql);
    }
}
