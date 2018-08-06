create table ASK_FOR_LEAVE
(
  ID                     NVARCHAR2(50) not null,
  START_DATE             NVARCHAR2(50),
  END_DATE               NVARCHAR2(50),
  TYPE                   NVARCHAR2(50),
  REASON                 NVARCHAR2(100),
  STATUS                 NUMBER(1),
  USID                    NVARCHAR2(50),
  PROPOSER               NVARCHAR2(50),
  CUR_PROCESSINSTANCE_ID NVARCHAR2(100),
  TASK_ID                NVARCHAR2(255),
  CREATE_TIME            DATE
);
alter table ASK_FOR_LEAVE
  add constraint ID_AFL_PK primary key (ID);

create table IP_DEV.GEN_SCHEME
(
  ID                   NVARCHAR2(64) not null,
  SCHEME_NAME          NVARCHAR2(200),
  PACKAGE_NAME         NVARCHAR2(500),
  MODULE_NAME          NVARCHAR2(30),
  SUB_MODULE_NAME      NVARCHAR2(30),
  FUNCTION_NAME        NVARCHAR2(500),
  FUNCTION_NAME_SIMPLE NVARCHAR2(100),
  FUNCTION_AUTHOR      NVARCHAR2(100),
  GEN_TABLE_ID         NVARCHAR2(200),
  CREATE_BY            NVARCHAR2(64),
  CREATE_DATE          DATE,
  UPDATE_BY            NVARCHAR2(64),
  UPDATE_DATE          DATE,
  REMARKS              NVARCHAR2(255),
  DEL_FLAG             NCHAR(1),
  SCHEMEtYPE           NVARCHAR2(10),
  REPLACEfILE          NVARCHAR2(10),
  CATEGORY             NVARCHAR2(2000)
);
comment on table GEN_SCHEME
  is '���ɷ���';
comment on column GEN_SCHEME.ID
  is '���';
comment on column GEN_SCHEME.SCHEME_NAME
  is '����';
comment on column GEN_SCHEME.PACKAGE_NAME
  is '���ɰ�·��';
comment on column GEN_SCHEME.MODULE_NAME
  is '����ģ����';
comment on column GEN_SCHEME.SUB_MODULE_NAME
  is '������ģ����';
comment on column GEN_SCHEME.FUNCTION_NAME
  is '���ɹ�����';
comment on column GEN_SCHEME.FUNCTION_NAME_SIMPLE
  is '���ɹ���������д��';
comment on column GEN_SCHEME.FUNCTION_AUTHOR
  is '���ɹ�������';
comment on column GEN_SCHEME.GEN_TABLE_ID
  is '���ɱ���';
comment on column GEN_SCHEME.CREATE_BY
  is '������';
comment on column GEN_SCHEME.CREATE_DATE
  is '����ʱ��';
comment on column GEN_SCHEME.UPDATE_BY
  is '������';
comment on column GEN_SCHEME.UPDATE_DATE
  is '����ʱ��';
comment on column GEN_SCHEME.REMARKS
  is '��ע��Ϣ';
comment on column GEN_SCHEME.DEL_FLAG
  is 'ɾ����ǣ�0��������1��ɾ����';
comment on column GEN_SCHEME.CATEGORY
  is '����';
alter table GEN_SCHEME
  add constraint ID_GS_PK primary key (ID);

create table GEN_TABLE
(
  ID              NVARCHAR2(64) not null,
  TABLE_NAME      NVARCHAR2(200),
  TABLE_COMMENTS  NVARCHAR2(500),
  CLASS_NAME      NVARCHAR2(100),
  PARENT_TABLE    NVARCHAR2(200),
  PARENT_TABLE_FK NVARCHAR2(100),
  CREATE_BY       NVARCHAR2(64),
  CREATE_DATE     DATE,
  UPDATE_BY       NVARCHAR2(64),
  UPDATE_DATE     DATE,
  REMARKS         NVARCHAR2(255),
  DEL_FLAG        NCHAR(1),
  LAYOUT          NVARCHAR2(30),
  ISgEN           NCHAR(1),
  HIRER_ID        NVARCHAR2(50)
);
comment on table GEN_TABLE
  is 'ҵ���';
comment on column GEN_TABLE.ID
  is '���';
comment on column GEN_TABLE.TABLE_NAME
  is '����';
comment on column GEN_TABLE.TABLE_COMMENTS
  is '����';
comment on column GEN_TABLE.CLASS_NAME
  is 'ʵ��������';
comment on column GEN_TABLE.PARENT_TABLE
  is '��������';
comment on column GEN_TABLE.PARENT_TABLE_FK
  is '�����������';
comment on column GEN_TABLE.CREATE_BY
  is '������';
comment on column GEN_TABLE.CREATE_DATE
  is '����ʱ��';
comment on column GEN_TABLE.UPDATE_BY
  is '������';
comment on column GEN_TABLE.UPDATE_DATE
  is '����ʱ��';
comment on column GEN_TABLE.REMARKS
  is '��ע��Ϣ';
comment on column GEN_TABLE.DEL_FLAG
  is 'ɾ����ǣ�0��������1��ɾ����';
comment on column GEN_TABLE.LAYOUT
  is 'ҳ�沼��';
alter table GEN_TABLE
  add constraint ID_GT_PK primary key (ID);

create table GEN_TABLE_COLUMN
(
  ID              NVARCHAR2(64) not null,
  GEN_TABLE_ID    NVARCHAR2(64),
  COLUMN_NAME     NVARCHAR2(200),
  COLUMN_COMMENTS NVARCHAR2(500),
  JDBC_TYPE       NVARCHAR2(100),
  JAVA_TYPE       NVARCHAR2(500),
  JAVA_FIELD      NVARCHAR2(200),
  IS_PK           NCHAR(1),
  IS_NULL         NCHAR(1),
  IS_INSERT       NCHAR(1),
  IS_EDIT         NCHAR(1),
  IS_LIST         NCHAR(1),
  IS_QUERY        NCHAR(1),
  QUERY_TYPE      NVARCHAR2(200),
  SHOW_TYPE       NVARCHAR2(200),
  DICT_TYPE       NVARCHAR2(200),
  SORT            NUMBER(10),
  CREATE_BY       NVARCHAR2(64),
  CREATE_DATE     DATE,
  UPDATE_BY       NVARCHAR2(64),
  UPDATE_DATE     DATE,
  REMARKS         NVARCHAR2(255),
  DEL_FLAG        NCHAR(1),
  ISuSE           NVARCHAR2(30),
  SETTINGS        NVARCHAR2(2000)
);
comment on table GEN_TABLE_COLUMN
  is 'ҵ����ֶ�';
comment on column GEN_TABLE_COLUMN.ID
  is '���';
comment on column GEN_TABLE_COLUMN.GEN_TABLE_ID
  is '��������';
comment on column GEN_TABLE_COLUMN.COLUMN_NAME
  is '����';
comment on column GEN_TABLE_COLUMN.COLUMN_COMMENTS
  is '����';
comment on column GEN_TABLE_COLUMN.JDBC_TYPE
  is '�е��������͵��ֽڳ���';
comment on column GEN_TABLE_COLUMN.JAVA_TYPE
  is 'java����';
comment on column GEN_TABLE_COLUMN.JAVA_FIELD
  is 'java�ֶ���';
comment on column GEN_TABLE_COLUMN.IS_PK
  is '�Ƿ�����';
comment on column GEN_TABLE_COLUMN.IS_NULL
  is '�Ƿ��Ϊ��';
comment on column GEN_TABLE_COLUMN.IS_INSERT
  is '�Ƿ�Ϊ�����ֶ�';
comment on column GEN_TABLE_COLUMN.IS_EDIT
  is '�Ƿ�༭�ֶ�';
comment on column GEN_TABLE_COLUMN.IS_LIST
  is '�Ƿ��б��ֶ�';
comment on column GEN_TABLE_COLUMN.IS_QUERY
  is '�Ƿ��ѯ�ֶ�';
comment on column GEN_TABLE_COLUMN.QUERY_TYPE
  is '��ѯ��ʽ�����ڡ������ڡ����ڡ�С�ڡ���Χ����like����like������like��';
comment on column IP_DEV.GEN_TABLE_COLUMN.SHOW_TYPE
  is '�ֶ����ɷ������ı����ı��������򡢸�ѡ�򡢵�ѡ���ֵ�ѡ����Աѡ�񡢲���ѡ������ѡ��';
comment on column GEN_TABLE_COLUMN.DICT_TYPE
  is '�ֵ�����';
comment on column GEN_TABLE_COLUMN.SORT
  is '��������';
comment on column GEN_TABLE_COLUMN.CREATE_BY
  is '������';
comment on column GEN_TABLE_COLUMN.CREATE_DATE
  is '����ʱ��';
comment on column GEN_TABLE_COLUMN.UPDATE_BY
  is '������';
comment on column GEN_TABLE_COLUMN.UPDATE_DATE
  is '����ʱ��';
comment on column GEN_TABLE_COLUMN.REMARKS
  is '��ע��Ϣ';
comment on column GEN_TABLE_COLUMN.DEL_FLAG
  is 'ɾ����ǣ�0��������1��ɾ����';
comment on column GEN_TABLE_COLUMN.SETTINGS
  is '�������ã���չ�ֶ�json��';
alter table GEN_TABLE_COLUMN
  add constraint ID_GTC_PK primary key (ID);

create table GEN_TEMPLATE
(
  ID          NVARCHAR2(64) not null,
  NAME        NVARCHAR2(200),
  FILE_PATH   NVARCHAR2(500),
  FILE_NAME   NVARCHAR2(200),
  CREATE_BY   NVARCHAR2(64),
  CREATE_DATE DATE,
  UPDATE_BY   NVARCHAR2(64),
  UPDATE_DATE DATE,
  REMARKS     NVARCHAR2(255),
  DEL_FLAG    NCHAR(1),
  CATEGORY    NVARCHAR2(2000),
  CONTENT     NVARCHAR2(2000)
);
comment on table GEN_TEMPLATE
  is '����ģ���';
comment on column GEN_TEMPLATE.ID
  is '���';
comment on column GEN_TEMPLATE.NAME
  is '����';
comment on column GEN_TEMPLATE.FILE_PATH
  is '�����ļ�·��';
comment on column GEN_TEMPLATE.FILE_NAME
  is '�����ļ���';
comment on column GEN_TEMPLATE.CREATE_BY
  is '������';
comment on column GEN_TEMPLATE.CREATE_DATE
  is '����ʱ��';
comment on column GEN_TEMPLATE.UPDATE_BY
  is '������';
comment on column GEN_TEMPLATE.UPDATE_DATE
  is '����ʱ��';
comment on column GEN_TEMPLATE.REMARKS
  is '��ע��Ϣ';
comment on column GEN_TEMPLATE.DEL_FLAG
  is 'ɾ����ǣ�0��������1��ɾ����';
comment on column GEN_TEMPLATE.CATEGORY
  is '����';
comment on column GEN_TEMPLATE.CONTENT
  is '����';
alter table GEN_TEMPLATE
  add constraint ID_GTT_PK primary key (ID);

create table IP_COMPANY
(
  CO_ID         NVARCHAR2(60) not null,
  HIRER_ID      NVARCHAR2(60),
  CO_CODE       NVARCHAR2(30),
  PARENT_CO_ID  NVARCHAR2(60),
  CO_NAME       NVARCHAR2(30),
  CO_FULLNAME   NVARCHAR2(50),
  LINKMAN       NVARCHAR2(20),
  IS_ENABLED    NVARCHAR2(1),
  CO_DESC       NVARCHAR2(300),
  LEVEL_NUM     NUMBER(2),
  DEPT_DETAIL   NVARCHAR2(300),
  DISP_ORDER    NUMBER(10) default 0,
  AGCFS_DW_TYPE NVARCHAR2(50),
  HOLD1         NVARCHAR2(50),
  HOLD2         NVARCHAR2(50),
  HOLD3         NVARCHAR2(50),
  HOLD4         NVARCHAR2(50),
  HOLD5         NVARCHAR2(50),
  HOLD6         NVARCHAR2(50),
  HOLD7         NVARCHAR2(50),
  HOLD8         NVARCHAR2(50),
  HOLD9         NVARCHAR2(50),
  HOLD10        NVARCHAR2(50),
  IS_FINANCIAL  NVARCHAR2(10),
  CO_NAME1      NVARCHAR2(120),
  CO_NAME2      NVARCHAR2(120),
  CO_NAME3      NVARCHAR2(120),
  CO_CODE_TMP   NVARCHAR2(30)
);
comment on column IP_COMPANY.HIRER_ID
  is '�⻧��';
comment on column IP_COMPANY.PARENT_CO_ID
  is '�ϼ���λ���';
comment on column IP_COMPANY.CO_NAME
  is '��λ����';
comment on column IP_COMPANY.CO_FULLNAME
  is '��λȫ��';
comment on column IP_COMPANY.LINKMAN
  is '��λ��ϵ��';
comment on column IP_COMPANY.CO_DESC
  is '����';
comment on column IP_COMPANY.LEVEL_NUM
  is '���ż���';
comment on column IP_COMPANY.DISP_ORDER
  is '˳�����';
comment on column IP_COMPANY.IS_FINANCIAL
  is '�Ƿ������λ';
comment on column IP_COMPANY.CO_NAME1
  is '����1';
comment on column IP_COMPANY.CO_NAME2
  is '����2';
comment on column IP_COMPANY.CO_NAME3
  is '����3';
comment on column IP_COMPANY.CO_CODE_TMP
  is '����ԭʼcode';
alter table IP_COMPANY
  add constraint CO_ID_IC_PK primary key (CO_ID);

create table IP_COMPANY_SYN
(
  CO_ID       NVARCHAR2(60),
  CO_CODE_TMP NVARCHAR2(60),
  HIRER_ID    NVARCHAR2(60)
);
comment on column IP_COMPANY_SYN.CO_ID
  is '����id';
comment on column IP_COMPANY_SYN.CO_CODE_TMP
  is '����ԭʼcode';
comment on column IP_COMPANY_SYN.HIRER_ID
  is '�⻧id';

create table IP_DATA_PARTITION
(
  DATA_ID     NVARCHAR2(60) not null,
  HOST        NVARCHAR2(100),
  PORT        NVARCHAR2(20),
  USER_NAME   NVARCHAR2(100),
  PASSWORD    NVARCHAR2(100),
  SCHEMA_NAME NVARCHAR2(100),
  AREA_NAME   NVARCHAR2(100),
  DB_DRIVER   NVARCHAR2(255),
  URL         NVARCHAR2(255),
  CREATE_DATE DATE,
  UPDATE_DATE DATE
);
comment on column IP_DATA_PARTITION.DATA_ID
  is '����';
comment on column IP_DATA_PARTITION.HOST
  is '������/IP';
comment on column IP_DATA_PARTITION.PORT
  is '�˿ں�';
comment on column IP_DATA_PARTITION.USER_NAME
  is '�û���';
comment on column IP_DATA_PARTITION.PASSWORD
  is '����';
comment on column IP_DATA_PARTITION.SCHEMA_NAME
  is '������/SCHEMA';
comment on column IP_DATA_PARTITION.DB_DRIVER
  is '���ݿ�����';
comment on column IP_DATA_PARTITION.URL
  is '���ݿ�URL������ƴ�ӣ�';
comment on column IP_DATA_PARTITION.CREATE_DATE
  is '����ʱ��';
comment on column IP_DATA_PARTITION.UPDATE_DATE
  is '����ʱ��';
alter table IP_DATA_PARTITION
  add constraint DATA_ID_IDP primary key (DATA_ID);

create table IP_DICTIONARY
(
  DIC_ID   NVARCHAR2(60) not null,
  DIC_TYPE NVARCHAR2(30),
  DIC_NAME NVARCHAR2(50)
);
comment on column IP_DICTIONARY.DIC_ID
  is '����id';
comment on column IP_DICTIONARY.DIC_TYPE
  is '�ֵ������';
comment on column IP_DICTIONARY.DIC_NAME
  is '�ֵ�����';
alter table IP_DICTIONARY
  add constraint DIC_ID_ID_PK primary key (DIC_ID);

create table IP_DEV.IP_DICTIONARY_DETAIL
(
  THE_ID      NVARCHAR2(60) not null,
  DIC_ID      NVARCHAR2(60),
  DETAIL_KEY  NVARCHAR2(60),
  DETAIL_INFO NVARCHAR2(100)
);
comment on column IP_DICTIONARY_DETAIL.THE_ID
  is '����id';
comment on column IP_DICTIONARY_DETAIL.DIC_ID
  is '�ֵ��id';
comment on column IP_DICTIONARY_DETAIL.DETAIL_KEY
  is '��ֵ';
comment on column IP_DICTIONARY_DETAIL.DETAIL_INFO
  is '�ֵ���ϸ��';
alter table IP_DICTIONARY_DETAIL
  add constraint THE_ID_IDD_PK primary key (THE_ID);

create table IP_HIRER
(
  HIRER_ID         NVARCHAR2(60),
  HIRER_NAME       NVARCHAR2(30),
  HIRER_SHORT_NAME NVARCHAR2(20),
  PASSWORD         NVARCHAR2(50),
  LINKMAN          NVARCHAR2(20),
  PHONE_NO         NVARCHAR2(14),
  CELLPHONE_NO     NVARCHAR2(14),
  SEX              NVARCHAR2(1),
  DUTY             NVARCHAR2(20),
  EMAIL            NVARCHAR2(30),
  FAX              NVARCHAR2(13),
  HIRER_TYPE       NVARCHAR2(10),
  REGION           NVARCHAR2(20),
  ADDRESS          NVARCHAR2(50),
  WEBSITE          NVARCHAR2(50),
  POSTCODE         NVARCHAR2(6),
  SALT             NVARCHAR2(50),
  LOGIN_TS         NUMBER(30),
  HIRER_NO         NVARCHAR2(30),
  CREATE_DATE      DATE,
  UPDATE_DATE      DATE,
  HIRER_LOGO_URL   NVARCHAR2(100),
  HIRER_PIC_URL    NVARCHAR2(100),
  LOGIN_NAME       NVARCHAR2(45),
  DATA_ID          NVARCHAR2(60),
  DB_URL           NVARCHAR2(255),
  IS_VALID         NVARCHAR2(10),
  DB_SCHEMA        NVARCHAR2(100),
  MYCAT_SCHEMA     NVARCHAR2(100),
  HIRER_CODE       NVARCHAR2(30)
);
comment on column IP_HIRER.HIRER_NAME
  is '�⻧����';
comment on column IP_HIRER.HIRER_SHORT_NAME
  is '�⻧���';
comment on column IP_HIRER.LINKMAN
  is '��ϵ������';
comment on column IP_HIRER.PHONE_NO
  is '��ϵ�˵绰';
comment on column IP_HIRER.CELLPHONE_NO
  is '��ϵ���ֻ�';
comment on column IP_HIRER.SEX
  is '��ϵ���Ա�';
comment on column IP_HIRER.DUTY
  is '��ϵ��ְ��';
comment on column IP_HIRER.HIRER_TYPE
  is '�⻧����';
comment on column IP_HIRER.REGION
  is '��������';
comment on column IP_HIRER.ADDRESS
  is 'ͨѶ��ַ';
comment on column IP_HIRER.WEBSITE
  is '��ַ';
comment on column IP_HIRER.POSTCODE
  is '�ʱ�';
comment on column IP_HIRER.HIRER_NO
  is '�⻧��';
comment on column IP_HIRER.CREATE_DATE
  is '����ʱ��';
comment on column IP_HIRER.UPDATE_DATE
  is '��������';
comment on column IP_HIRER.HIRER_LOGO_URL
  is '�⻧LOGO·��';
comment on column IP_HIRER.HIRER_PIC_URL
  is '�⻧��������ͷ��';
comment on column IP_HIRER.LOGIN_NAME
  is '��½�˺�';
comment on column IP_HIRER.DATA_ID
  is '������ip_data_partition������ID';
comment on column IP_HIRER.DB_URL
  is '�⻧����schema';
comment on column IP_HIRER.IS_VALID
  is '�Ƿ���Ч 0δ��Ч 1����Ч';
comment on column IP_HIRER.DB_SCHEMA
  is '��Ӧ��mysql���ݿ��schema';
comment on column IP_HIRER.MYCAT_SCHEMA
  is '��Ӧmycat��schema';

create table IP_HIRER_SYN
(
  HIRER_ID     NVARCHAR2(60),
  CELLPHONE_NO NVARCHAR2(60)
);
comment on column IP_HIRER_SYN.HIRER_ID
  is '�⻧ID';
comment on column IP_HIRER_SYN.CELLPHONE_NO
  is '�⻧�绰����';

create table IP_INDEX_CONFIG
(
  CONFIG_ID      NVARCHAR2(60) not null,
  MENU_ID        NVARCHAR2(60),
  MENU_NAME      NVARCHAR2(100),
  CATALOG        NVARCHAR2(20),
  ROUTER_ADDR    NVARCHAR2(100),
  INTERFACE_ADDR NVARCHAR2(100),
  INTER_PARAM    NVARCHAR2(100),
  ISuSE          NVARCHAR2(6)
);
comment on column IP_INDEX_CONFIG.CONFIG_ID
  is '����';
comment on column IP_INDEX_CONFIG.MENU_NAME
  is 'Ȩ�޲˵���';
comment on column IP_INDEX_CONFIG.CATALOG
  is '��������';
comment on column IP_INDEX_CONFIG.ROUTER_ADDR
  is '·�ɵ�ַ';
comment on column IP_INDEX_CONFIG.INTERFACE_ADDR
  is '�ӿڵ�ַ';
comment on column IP_INDEX_CONFIG.INTER_PARAM
  is '�ӿڲ�������';
alter table IP_INDEX_CONFIG
  add constraint CONFIG_ID_IIC_PK primary key (CONFIG_ID);

create table IP_MENU
(
  MENU_ID          NVARCHAR2(60) not null,
  PARENT_MENU_NAME NVARCHAR2(50),
  PARENT_MENU_ID   NVARCHAR2(60),
  MENU_NAME        NVARCHAR2(20),
  LEVEL_NUM        NVARCHAR2(10),
  DISP_ORDER       NUMBER(9),
  IS_LEAF          NVARCHAR2(1),
  URL              NVARCHAR2(320),
  AUTH_LEVEL       NVARCHAR2(1),
  IS_SHOW          NVARCHAR2(1),
  MENU_DESC        NVARCHAR2(40),
  IS_JUMP          NVARCHAR2(1),
  MENU_LOGO        NVARCHAR2(100),
  PERMISSION       NVARCHAR2(200)
);
comment on column IP_MENU.PARENT_MENU_ID
  is '�ϼ��˵����';
comment on column IP_MENU.LEVEL_NUM
  is '�˵��㼶(ֵ��1��2��3)';
comment on column IP_MENU.DISP_ORDER
  is '�����';
comment on column IP_MENU.IS_LEAF
  is '�Ƿ�ĩ���ڵ�(1-�ǣ�0-��)';
comment on column IP_MENU.URL
  is '�˵�URL';
comment on column IP_MENU.AUTH_LEVEL
  is '��Ȩ����(0,1,2,3)';
comment on column IP_MENU.IS_SHOW
  is '�Ƿ���ʾ(1��,0��)';
comment on column IP_MENU.MENU_DESC
  is '�˵�����';
comment on column IP_MENU.MENU_LOGO
  is '�˵�logo�ģ���Ӧǰ�˵�class';
comment on column IP_MENU.PERMISSION
  is 'controller�������ԴȨ�ޱ�ʶ';
alter table IP_MENU
  add constraint MENU_ID_IM primary key (MENU_ID);

create table IP_MENU_ICON
(
  MENU_ID   NVARCHAR2(60) not null,
  MENU_NAME NVARCHAR2(20),
  URL       NVARCHAR2(320),
  USER_ID   NVARCHAR2(60) not null,
  ICON_ID   NVARCHAR2(100),
  ICON_PATH NVARCHAR2(320)
);
alter table IP_MENU_ICON
  add constraint MI_PK primary key (MENU_ID, USER_ID);

create table IP_NOTICE
(
  NOTICE_ID      NVARCHAR2(100),
  HIRER_ID       NVARCHAR2(100),
  ATTACHMENT_URL NVARCHAR2(500),
  NOTICE_TITLE   NVARCHAR2(50),
  ISPUBLISH      NVARCHAR2(1),
  CREATE_DATE    DATE,
  UPDATE_DATE    DATE,
  NOTICE_CONTENT VARCHAR2(3000)
);
comment on column IP_NOTICE.ATTACHMENT_URL
  is '����·��';
comment on column IP_NOTICE.NOTICE_TITLE
  is '�������';
comment on column IP_NOTICE.ISPUBLISH
  is '�Ƿ񷢲�(0-δ������ֻ����,1-����)';
comment on column IP_NOTICE.CREATE_DATE
  is '����ʱ��';
comment on column IP_NOTICE.UPDATE_DATE
  is '����ʱ��';
comment on column IP_NOTICE.NOTICE_CONTENT
  is '��������';

create table IP_REGION
(
  THE_ID      NVARCHAR2(60) not null,
  THE_CODE    NVARCHAR2(60),
  THE_NAME    NVARCHAR2(100),
  PARENT_ID   NVARCHAR2(60),
  IS_VALID    NVARCHAR2(2),
  CREATE_DATE DATE,
  UPDATE_DATE DATE
);
comment on column IP_REGION.THE_ID
  is '����ID';
comment on column IP_REGION.THE_CODE
  is '��������';
comment on column IP_REGION.THE_NAME
  is '��������';
comment on column IP_REGION.PARENT_ID
  is '����ID';
comment on column IP_REGION.IS_VALID
  is '�Ƿ���������,0:δ���� 1:������';
comment on column IP_REGION.CREATE_DATE
  is '����ʱ��';
comment on column IP_REGION.UPDATE_DATE
  is '����ʱ��';
alter table IP_REGION
  add constraint THE_ID_IR_PK primary key (THE_ID);

create table IP_ROLE
(
  ROLE_ID    NVARCHAR2(60) not null,
  HIRER_ID   NVARCHAR2(60),
  CO_ID      NVARCHAR2(60),
  ROLE_NAME  NVARCHAR2(20),
  ROLE_TYPE  NVARCHAR2(2),
  ROLE_DESC  NVARCHAR2(300),
  DISP_ORDER NUMBER(10),
  ROLE_CODE  NVARCHAR2(30)
);
comment on column IP_ROLE.ROLE_DESC
  is '��ɫ����';
comment on column IP_ROLE.DISP_ORDER
  is '�������';
alter table IP_ROLE
  add constraint ROLE_ID_IR_PK primary key (ROLE_ID);

create table IP_ROLE_MENU
(
  THE_ID    NVARCHAR2(60) not null,
  MENU_ID   NVARCHAR2(60),
  ROLE_ID   NVARCHAR2(60),
  MENU_NAME NVARCHAR2(20)
);
alter table IP_ROLE_MENU
  add constraint THE_ID_IRM_PK primary key (THE_ID);

create table IP_ROLE_SYN
(
  ROLE_ID   NVARCHAR2(60),
  ROLE_CODE NVARCHAR2(60),
  HIRER_ID  NVARCHAR2(60)
);
comment on column IP_ROLE_SYN.ROLE_ID
  is '��ɫid';
comment on column IP_ROLE_SYN.ROLE_CODE
  is '��ɫcode';
comment on column IP_ROLE_SYN.HIRER_ID
  is '�⻧id';

create table IP_USER
(
  USER_ID          NVARCHAR2(60),
  HIRER_ID         NVARCHAR2(60),
  LOGIN_NAME       NVARCHAR2(20),
  USER_NAME        NVARCHAR2(20),
  USER_SEX         NVARCHAR2(1),
  PASSWORD         NVARCHAR2(50),
  USER_EMAIL       NVARCHAR2(30),
  PHONE_NO         NVARCHAR2(14),
  CELLPHONE_NO     NVARCHAR2(30),
  IS_ENABLED       NVARCHAR2(20),
  EMPLOYEE_NO      NVARCHAR2(20),
  DUTY             NVARCHAR2(20),
  NATIVE_PLACE     NVARCHAR2(40),
  GRADUATE_SCHOOL  NVARCHAR2(20),
  MAJOR            NVARCHAR2(30),
  EDUCATION        NVARCHAR2(20),
  GRADUATIOIN_TIME DATE,
  REMARK           NVARCHAR2(50),
  SALT             NVARCHAR2(50),
  LOGIN_TS         NUMBER(30),
  USER_PIC_URL     NVARCHAR2(100),
  EXTENSION        NVARCHAR2(45),
  USER_CODE        NVARCHAR2(30),
  USER_TYPE        NVARCHAR2(2)
);
comment on column IP_USER.LOGIN_NAME
  is '��¼��';
comment on column IP_USER.USER_SEX
  is '�û��Ա� ��1��0Ů��';
comment on column IP_USER.PHONE_NO
  is '�绰����';
comment on column IP_USER.CELLPHONE_NO
  is '�ֻ�����';
comment on column IP_USER.EMPLOYEE_NO
  is 'Ա����';
comment on column IP_USER.DUTY
  is 'ְ��';
comment on column IP_USER.NATIVE_PLACE
  is 'ԭ��';
comment on column IP_USER.GRADUATE_SCHOOL
  is '��ҵѧУ';
comment on column IP_USER.MAJOR
  is 'רҵ';
comment on column IP_USER.EDUCATION
  is 'ѧ��';
comment on column IP_USER.GRADUATIOIN_TIME
  is '��ҵʱ��';
comment on column IP_USER.USER_PIC_URL
  is '�û�ͷ��';
comment on column IP_USER.USER_TYPE
  is '�û����� 0���⻧����Ա  1����ͨ�û�';

create table IP_USER_COMPANY
(
  THE_ID  NVARCHAR2(60) not null,
  USER_ID NVARCHAR2(60),
  CO_ID   NVARCHAR2(60),
  CO_CODE NVARCHAR2(30),
  CO_NAME NVARCHAR2(30)
);
alter table IP_USER_COMPANY
  add constraint THE_ID_IUC_PK primary key (THE_ID);

create table IP_USER_HIDE_MENU
(
  USER_ID NVARCHAR2(60),
  MENU_ID NVARCHAR2(60)
);
comment on column IP_USER_HIDE_MENU.USER_ID
  is '�û�id�������û�����⻧��';
comment on column IP_USER_HIDE_MENU.MENU_ID
  is '�˵�id�������˵���';

create table IP_USER_ROLE
(
  THE_ID       NVARCHAR2(60) not null,
  USER_ID      NVARCHAR2(60),
  ROLE_ID      NVARCHAR2(60),
  CO_ID        NVARCHAR2(60),
  IS_PART_TIME NVARCHAR2(2),
  DISP_ORDER   NUMBER(10)
);
comment on column IP_USER_ROLE.IS_PART_TIME
  is '�Ƿ��ְ0�����Ǽ�ְ  1����ְ';
comment on column IP_USER_ROLE.DISP_ORDER
  is '�������';
alter table IP_USER_ROLE
  add constraint THE_ID_IUR_PK primary key (THE_ID);

create table TICKET_DETAIL
(
  ID          NVARCHAR2(60) not null,
  BUSINESS_ID NVARCHAR2(60),
  STATUS      NUMBER(1),
  USID        NVARCHAR2(60),
  SUGGESTION  NVARCHAR2(60),
  BPM_UID     NVARCHAR2(100),
  CREATE_TIME DATE
);
alter table TICKET_DETAIL
  add constraint ID_TD_PK primary key (ID);

