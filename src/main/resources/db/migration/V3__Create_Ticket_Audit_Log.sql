CREATE TABLE ticket_audit_log (
    log_id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    action VARCHAR(10) NOT NULL,
    old_category VARCHAR(50),
    new_category VARCHAR(50),
    old_priority VARCHAR(50),
    new_priority VARCHAR(50),
    old_status VARCHAR(50),
    new_status VARCHAR(50),
    old_assignee_id BIGINT,
    new_assignee_id BIGINT,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_ticket_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO ticket_audit_log(
            ticket_id, action,
            new_category, new_priority, new_status, new_assignee_id
        )
        VALUES (
            NEW.id, 'INSERT',
            NEW.category, NEW.priority, NEW.ticket_status, NEW.assignee_id
        );
        RETURN NEW;

    ELSIF (TG_OP = 'UPDATE') THEN
        IF (OLD.category IS DISTINCT FROM NEW.category OR
            OLD.priority IS DISTINCT FROM NEW.priority OR
            OLD.ticket_status IS DISTINCT FROM NEW.ticket_status OR
            OLD.assignee_id IS DISTINCT FROM NEW.assignee_id) THEN

            INSERT INTO ticket_audit_log(
                ticket_id, action,
                old_category, new_category,
                old_priority, new_priority,
                old_status, new_status,
                old_assignee_id, new_assignee_id
            )
            VALUES (
                NEW.id, 'UPDATE',
                OLD.category, NEW.category,
                OLD.priority, NEW.priority,
                OLD.ticket_status, NEW.ticket_status,
                OLD.assignee_id, NEW.assignee_id
            );
        END IF;
        RETURN NEW;

    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO ticket_audit_log(
            ticket_id, action,
            old_category, old_priority, old_status, old_assignee_id
        )
        VALUES (
            OLD.id, 'DELETE',
            OLD.category, OLD.priority, OLD.ticket_status, OLD.assignee_id
        );
        RETURN OLD;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ticket_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON tickets
FOR EACH ROW EXECUTE FUNCTION log_ticket_changes();