CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT ck_users_role CHECK (role IN ('USER', 'ADMIN', 'MANAGER'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE partners (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    business_number VARCHAR(20) NOT NULL,
    partner_type VARCHAR(20) NOT NULL,
    contact_name VARCHAR(50) NULL,
    phone VARCHAR(30) NULL,
    email VARCHAR(100) NULL,
    address VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_partners PRIMARY KEY (id),
    CONSTRAINT uk_partners_business_number UNIQUE (business_number),
    CONSTRAINT ck_partners_type CHECK (partner_type IN ('SUPPLIER', 'CUSTOMER', 'BOTH')),
    CONSTRAINT ck_partners_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uk_categories_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    item_code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    category_id BIGINT NOT NULL,
    unit VARCHAR(20) NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    safety_stock INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT uk_items_item_code UNIQUE (item_code),
    CONSTRAINT fk_items_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT ck_items_unit_price CHECK (unit_price >= 0),
    CONSTRAINT ck_items_safety_stock CHECK (safety_stock >= 0),
    CONSTRAINT ck_items_status CHECK (status IN ('ACTIVE', 'DISCONTINUED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE purchase_orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_number VARCHAR(30) NOT NULL,
    partner_id BIGINT NOT NULL,
    writer_id BIGINT NOT NULL,
    approver_id BIGINT NULL,
    order_date DATE NOT NULL,
    due_date DATE NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    reject_reason VARCHAR(500) NULL,
    version BIGINT NOT NULL,
    approved_at DATETIME(6) NULL,
    received_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_purchase_orders PRIMARY KEY (id),
    CONSTRAINT uk_po_order_number UNIQUE (order_number),
    CONSTRAINT fk_po_partner FOREIGN KEY (partner_id) REFERENCES partners (id),
    CONSTRAINT fk_po_writer FOREIGN KEY (writer_id) REFERENCES users (id),
    CONSTRAINT fk_po_approver FOREIGN KEY (approver_id) REFERENCES users (id),
    CONSTRAINT ck_po_total_amount CHECK (total_amount >= 0),
    CONSTRAINT ck_po_status CHECK (status IN ('DRAFT', 'REQUESTED', 'APPROVED', 'REJECTED', 'RECEIVED', 'CANCELED')),
    INDEX idx_po_writer (writer_id),
    INDEX idx_po_status (status),
    INDEX idx_po_partner (partner_id),
    INDEX idx_po_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE purchase_order_lines (
    id BIGINT NOT NULL AUTO_INCREMENT,
    purchase_order_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    line_amount DECIMAL(15, 2) NOT NULL,
    CONSTRAINT pk_purchase_order_lines PRIMARY KEY (id),
    CONSTRAINT fk_pol_po FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders (id),
    CONSTRAINT fk_pol_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT ck_pol_quantity CHECK (quantity > 0),
    CONSTRAINT ck_pol_unit_price CHECK (unit_price >= 0),
    CONSTRAINT ck_pol_line_amount CHECK (line_amount >= 0),
    INDEX idx_pol_po (purchase_order_id),
    INDEX idx_pol_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE stocks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    version BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NULL,
    CONSTRAINT pk_stocks PRIMARY KEY (id),
    CONSTRAINT uk_stock_item UNIQUE (item_id),
    CONSTRAINT fk_stocks_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT ck_stocks_quantity CHECK (quantity >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
