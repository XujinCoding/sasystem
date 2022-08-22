select * from SA_DB.BATCH_TASK;

create tablespace TBLCUSTOMER
    datafile '/opt/oracle/oradata/tblcustomer.dbf'
    size 50 M reuse autoextend on next 51200 k MAXSIZE 20000 M
    EXTENT MANAGEMENT LOCAL AUTOALLOCATE;