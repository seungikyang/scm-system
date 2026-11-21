CREATE TABLE sales_orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_number VARCHAR(30) NOT NULL,
    partner_id BIGINT NOT NULL,
    writer_id BIGINT NOT NULL,
    manager_id BIGINT NULL,
    order_date DATE NOT NULL,
    ship_date DATE NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    cancel_reason VARCHAR(500) NULL,
    version BIGINT NOT NULL,
    confirmed_at DATETIME(6) NULL,
    shipped_at DATETIME(6) NULL,
    completed_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_sales_orders PRIMARY KEY (id),
    CONSTRAINT uk_so_order_number UNIQUE (order_number),
    CONSTRAINT fk_so_partner FOREIGN KEY (partner_id) REFERENCES partners(id),
    CONSTRAINT fk_so_writer FOREIGN KEY (writer_id) REFERENCES users(id),
    CONSTRAINT fk_so_manager FOREIGN KEY (manager_id) REFERENCES users(id),
    CONSTRAINT ck_so_total CHECK (total_amount >= 0),
    CONSTRAINT ck_so_status CHECK (status IN ('DRAFT','CONFIRMED','SHIPPED','COMPLETED','CANCELED')),
    INDEX idx_so_writer (writer_id),
    INDEX idx_so_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE sales_order_lines (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sales_order_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL,
    line_amount DECIMAL(15,2) NOT NULL,
    CONSTRAINT pk_sales_order_lines PRIMARY KEY(id),
    CONSTRAINT fk_sol_so FOREIGN KEY(sales_order_id) REFERENCES sales_orders(id),
    CONSTRAINT fk_sol_item FOREIGN KEY(item_id) REFERENCES items(id),
    CONSTRAINT ck_sol_quantity CHECK(quantity > 0),
    CONSTRAINT ck_sol_price CHECK(unit_price >= 0),
    CONSTRAINT ck_sol_amount CHECK(line_amount >= 0),
    INDEX idx_sol_so (sales_order_id),
    INDEX idx_sol_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE notices (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    writer_id BIGINT NOT NULL,
    important BOOLEAN NOT NULL,
    view_count BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_notices PRIMARY KEY(id),
    CONSTRAINT fk_notice_writer FOREIGN KEY(writer_id) REFERENCES users(id),
    CONSTRAINT ck_notice_views CHECK(view_count >= 0),
    INDEX idx_notice_priority (important, created_at, id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
