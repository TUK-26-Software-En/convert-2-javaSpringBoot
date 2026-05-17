insert into books (title, author, isbn, publisher, published_date, total_quantity, available_quantity, status, created_at, updated_at)
values
    ('Clean Architecture', 'Robert C. Martin', '9780134494166', 'Prentice Hall', date '2017-09-20', 4, 4, 'AVAILABLE', current_timestamp, current_timestamp),
    ('Domain-Driven Design', 'Eric Evans', '9780321125217', 'Addison-Wesley', date '2003-08-30', 2, 2, 'AVAILABLE', current_timestamp, current_timestamp),
    ('Refactoring', 'Martin Fowler', '9780134757599', 'Addison-Wesley', date '2018-11-19', 3, 3, 'AVAILABLE', current_timestamp, current_timestamp),
    ('Designing Data-Intensive Applications', 'Martin Kleppmann', '9781449373320', 'O''Reilly Media', date '2017-03-16', 2, 2, 'AVAILABLE', current_timestamp, current_timestamp),
    ('Effective Java', 'Joshua Bloch', '9780134685991', 'Addison-Wesley', date '2018-01-06', 5, 5, 'AVAILABLE', current_timestamp, current_timestamp);

insert into members (name, email, phone_number, status, created_at, updated_at)
values
    ('Alice Kim', 'alice.kim@example.com', '010-2000-0001', 'ACTIVE', current_timestamp, current_timestamp),
    ('Brian Lee', 'brian.lee@example.com', '010-2000-0002', 'ACTIVE', current_timestamp, current_timestamp),
    ('Mina Choi', 'mina.choi@example.com', '010-2000-0003', 'ACTIVE', current_timestamp, current_timestamp),
    ('Dormant User', 'dormant.user@example.com', '010-2000-0004', 'INACTIVE', current_timestamp, current_timestamp);

insert into loans (book_id, member_id, loaned_at, due_date, returned_at, status)
values
    (
        (select id from books where isbn = '9780134494166'),
        (select id from members where email = 'alice.kim@example.com'),
        current_timestamp - interval '2 days',
        (current_date + interval '5 days')::date,
        null,
        'ACTIVE'
    ),
    (
        (select id from books where isbn = '9780321125217'),
        (select id from members where email = 'brian.lee@example.com'),
        current_timestamp - interval '5 days',
        (current_date + interval '2 days')::date,
        null,
        'ACTIVE'
    ),
    (
        (select id from books where isbn = '9781449373320'),
        (select id from members where email = 'mina.choi@example.com'),
        current_timestamp - interval '12 days',
        (current_date - interval '3 days')::date,
        null,
        'ACTIVE'
    ),
    (
        (select id from books where isbn = '9780134757599'),
        (select id from members where email = 'alice.kim@example.com'),
        current_timestamp - interval '15 days',
        (current_date - interval '8 days')::date,
        current_timestamp - interval '7 days',
        'RETURNED'
    ),
    (
        (select id from books where isbn = '9780134685991'),
        (select id from members where email = 'brian.lee@example.com'),
        current_timestamp - interval '20 days',
        (current_date - interval '10 days')::date,
        current_timestamp - interval '4 days',
        'RETURNED'
    );

update books book
set available_quantity = greatest(
        0,
        book.total_quantity - coalesce((
            select count(*)
            from loans loan
            where loan.book_id = book.id
              and loan.status = 'ACTIVE'
        ), 0)
    ),
    status = case
        when book.total_quantity - coalesce((
            select count(*)
            from loans loan
            where loan.book_id = book.id
              and loan.status = 'ACTIVE'
        ), 0) > 0 then 'AVAILABLE'
        else 'UNAVAILABLE'
    end,
    updated_at = current_timestamp;
