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
  is '生成方案';
comment on column GEN_SCHEME.ID
  is '编号';
comment on column GEN_SCHEME.SCHEME_NAME
  is '名称';
comment on column GEN_SCHEME.PACKAGE_NAME
  is '生成包路径';
comment on column GEN_SCHEME.MODULE_NAME
  is '生成模块名';
comment on column GEN_SCHEME.SUB_MODULE_NAME
  is '生成子模块名';
comment on column GEN_SCHEME.FUNCTION_NAME
  is '生成功能名';
comment on column GEN_SCHEME.FUNCTION_NAME_SIMPLE
  is '生成功能名（简写）';
comment on column GEN_SCHEME.FUNCTION_AUTHOR
  is '生成功能作者';
comment on column GEN_SCHEME.GEN_TABLE_ID
  is '生成表编号';
comment on column GEN_SCHEME.CREATE_BY
  is '创建者';
comment on column GEN_SCHEME.CREATE_DATE
  is '创建时间';
comment on column GEN_SCHEME.UPDATE_BY
  is '更新者';
comment on column GEN_SCHEME.UPDATE_DATE
  is '更新时间';
comment on column GEN_SCHEME.REMARKS
  is '备注信息';
comment on column GEN_SCHEME.DEL_FLAG
  is '删除标记（0：正常；1：删除）';
comment on column GEN_SCHEME.CATEGORY
  is '分类';
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
  is '业务表';
comment on column GEN_TABLE.ID
  is '编号';
comment on column GEN_TABLE.TABLE_NAME
  is '名称';
comment on column GEN_TABLE.TABLE_COMMENTS
  is '描述';
comment on column GEN_TABLE.CLASS_NAME
  is '实体类名称';
comment on column GEN_TABLE.PARENT_TABLE
  is '关联父表';
comment on column GEN_TABLE.PARENT_TABLE_FK
  is '关联父表外键';
comment on column GEN_TABLE.CREATE_BY
  is '创建者';
comment on column GEN_TABLE.CREATE_DATE
  is '创建时间';
comment on column GEN_TABLE.UPDATE_BY
  is '更新者';
comment on column GEN_TABLE.UPDATE_DATE
  is '更新时间';
comment on column GEN_TABLE.REMARKS
  is '备注信息';
comment on column GEN_TABLE.DEL_FLAG
  is '删除标记（0：正常；1：删除）';
comment on column GEN_TABLE.LAYOUT
  is '页面布局';
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
  is '业务表字段';
comment on column GEN_TABLE_COLUMN.ID
  is '编号';
comment on column GEN_TABLE_COLUMN.GEN_TABLE_ID
  is '归属表编号';
comment on column GEN_TABLE_COLUMN.COLUMN_NAME
  is '名称';
comment on column GEN_TABLE_COLUMN.COLUMN_COMMENTS
  is '描述';
comment on column GEN_TABLE_COLUMN.JDBC_TYPE
  is '列的数据类型的字节长度';
comment on column GEN_TABLE_COLUMN.JAVA_TYPE
  is 'java类型';
comment on column GEN_TABLE_COLUMN.JAVA_FIELD
  is 'java字段名';
comment on column GEN_TABLE_COLUMN.IS_PK
  is '是否主键';
comment on column GEN_TABLE_COLUMN.IS_NULL
  is '是否可为空';
comment on column GEN_TABLE_COLUMN.IS_INSERT
  is '是否为插入字段';
comment on column GEN_TABLE_COLUMN.IS_EDIT
  is '是否编辑字段';
comment on column GEN_TABLE_COLUMN.IS_LIST
  is '是否列表字段';
comment on column GEN_TABLE_COLUMN.IS_QUERY
  is '是否查询字段';
comment on column GEN_TABLE_COLUMN.QUERY_TYPE
  is '查询方式（等于、不等于、大于、小于、范围、左like、右like、左右like）';
comment on column IP_DEV.GEN_TABLE_COLUMN.SHOW_TYPE
  is '字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）';
comment on column GEN_TABLE_COLUMN.DICT_TYPE
  is '字典类型';
comment on column GEN_TABLE_COLUMN.SORT
  is '排序（升序）';
comment on column GEN_TABLE_COLUMN.CREATE_BY
  is '创建者';
comment on column GEN_TABLE_COLUMN.CREATE_DATE
  is '创建时间';
comment on column GEN_TABLE_COLUMN.UPDATE_BY
  is '更新者';
comment on column GEN_TABLE_COLUMN.UPDATE_DATE
  is '更新时间';
comment on column GEN_TABLE_COLUMN.REMARKS
  is '备注信息';
comment on column GEN_TABLE_COLUMN.DEL_FLAG
  is '删除标记（0：正常；1：删除）';
comment on column GEN_TABLE_COLUMN.SETTINGS
  is '其它设置（扩展字段json）';
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
  is '代码模板表';
comment on column GEN_TEMPLATE.ID
  is '编号';
comment on column GEN_TEMPLATE.NAME
  is '名称';
comment on column GEN_TEMPLATE.FILE_PATH
  is '生成文件路径';
comment on column GEN_TEMPLATE.FILE_NAME
  is '生成文件名';
comment on column GEN_TEMPLATE.CREATE_BY
  is '创建者';
comment on column GEN_TEMPLATE.CREATE_DATE
  is '创建时间';
comment on column GEN_TEMPLATE.UPDATE_BY
  is '更新者';
comment on column GEN_TEMPLATE.UPDATE_DATE
  is '更新时间';
comment on column GEN_TEMPLATE.REMARKS
  is '备注信息';
comment on column GEN_TEMPLATE.DEL_FLAG
  is '删除标记（0：正常；1：删除）';
comment on column GEN_TEMPLATE.CATEGORY
  is '分类';
comment on column GEN_TEMPLATE.CONTENT
  is '内容';
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
  is '租户号';
comment on column IP_COMPANY.PARENT_CO_ID
  is '上级单位编号';
comment on column IP_COMPANY.CO_NAME
  is '单位名称';
comment on column IP_COMPANY.CO_FULLNAME
  is '单位全称';
comment on column IP_COMPANY.LINKMAN
  is '单位联系人';
comment on column IP_COMPANY.CO_DESC
  is '描述';
comment on column IP_COMPANY.LEVEL_NUM
  is '部门级别';
comment on column IP_COMPANY.DISP_ORDER
  is '顺序序号';
comment on column IP_COMPANY.IS_FINANCIAL
  is '是否财政单位';
comment on column IP_COMPANY.CO_NAME1
  is '别名1';
comment on column IP_COMPANY.CO_NAME2
  is '别名2';
comment on column IP_COMPANY.CO_NAME3
  is '别名3';
comment on column IP_COMPANY.CO_CODE_TMP
  is '部门原始code';
alter table IP_COMPANY
  add constraint CO_ID_IC_PK primary key (CO_ID);

create table IP_COMPANY_SYN
(
  CO_ID       NVARCHAR2(60),
  CO_CODE_TMP NVARCHAR2(60),
  HIRER_ID    NVARCHAR2(60)
);
comment on column IP_COMPANY_SYN.CO_ID
  is '部门id';
comment on column IP_COMPANY_SYN.CO_CODE_TMP
  is '部门原始code';
comment on column IP_COMPANY_SYN.HIRER_ID
  is '租户id';

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
  is '主键';
comment on column IP_DATA_PARTITION.HOST
  is '主机名/IP';
comment on column IP_DATA_PARTITION.PORT
  is '端口号';
comment on column IP_DATA_PARTITION.USER_NAME
  is '用户名';
comment on column IP_DATA_PARTITION.PASSWORD
  is '密码';
comment on column IP_DATA_PARTITION.SCHEMA_NAME
  is '服务名/SCHEMA';
comment on column IP_DATA_PARTITION.DB_DRIVER
  is '数据库驱动';
comment on column IP_DATA_PARTITION.URL
  is '数据库URL（程序拼接）';
comment on column IP_DATA_PARTITION.CREATE_DATE
  is '创建时间';
comment on column IP_DATA_PARTITION.UPDATE_DATE
  is '更新时间';
alter table IP_DATA_PARTITION
  add constraint DATA_ID_IDP primary key (DATA_ID);

create table IP_DICTIONARY
(
  DIC_ID   NVARCHAR2(60) not null,
  DIC_TYPE NVARCHAR2(30),
  DIC_NAME NVARCHAR2(50)
);
comment on column IP_DICTIONARY.DIC_ID
  is '主键id';
comment on column IP_DICTIONARY.DIC_TYPE
  is '字典表类型';
comment on column IP_DICTIONARY.DIC_NAME
  is '字典名称';
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
  is '主键id';
comment on column IP_DICTIONARY_DETAIL.DIC_ID
  is '字典表id';
comment on column IP_DICTIONARY_DETAIL.DETAIL_KEY
  is '键值';
comment on column IP_DICTIONARY_DETAIL.DETAIL_INFO
  is '字典详细信';
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
  is '租户名称';
comment on column IP_HIRER.HIRER_SHORT_NAME
  is '租户简称';
comment on column IP_HIRER.LINKMAN
  is '联系人姓名';
comment on column IP_HIRER.PHONE_NO
  is '联系人电话';
comment on column IP_HIRER.CELLPHONE_NO
  is '联系人手机';
comment on column IP_HIRER.SEX
  is '联系人性别';
comment on column IP_HIRER.DUTY
  is '联系人职务';
comment on column IP_HIRER.HIRER_TYPE
  is '租户类型';
comment on column IP_HIRER.REGION
  is '所属地区';
comment on column IP_HIRER.ADDRESS
  is '通讯地址';
comment on column IP_HIRER.WEBSITE
  is '网址';
comment on column IP_HIRER.POSTCODE
  is '邮编';
comment on column IP_HIRER.HIRER_NO
  is '租户号';
comment on column IP_HIRER.CREATE_DATE
  is '创建时间';
comment on column IP_HIRER.UPDATE_DATE
  is '更新日期';
comment on column IP_HIRER.HIRER_LOGO_URL
  is '租户LOGO路径';
comment on column IP_HIRER.HIRER_PIC_URL
  is '租户个人设置头像';
comment on column IP_HIRER.LOGIN_NAME
  is '登陆账号';
comment on column IP_HIRER.DATA_ID
  is '分区表ip_data_partition的主键ID';
comment on column IP_HIRER.DB_URL
  is '租户所属schema';
comment on column IP_HIRER.IS_VALID
  is '是否生效 0未生效 1已生效';
comment on column IP_HIRER.DB_SCHEMA
  is '对应的mysql数据库的schema';
comment on column IP_HIRER.MYCAT_SCHEMA
  is '对应mycat的schema';

create table IP_HIRER_SYN
(
  HIRER_ID     NVARCHAR2(60),
  CELLPHONE_NO NVARCHAR2(60)
);
comment on column IP_HIRER_SYN.HIRER_ID
  is '租户ID';
comment on column IP_HIRER_SYN.CELLPHONE_NO
  is '租户电话号码';

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
  is '主键';
comment on column IP_INDEX_CONFIG.MENU_NAME
  is '权限菜单名';
comment on column IP_INDEX_CONFIG.CATALOG
  is '索引分类';
comment on column IP_INDEX_CONFIG.ROUTER_ADDR
  is '路由地址';
comment on column IP_INDEX_CONFIG.INTERFACE_ADDR
  is '接口地址';
comment on column IP_INDEX_CONFIG.INTER_PARAM
  is '接口参数名称';
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
  is '上级菜单编号';
comment on column IP_MENU.LEVEL_NUM
  is '菜单层级(值集1，2，3)';
comment on column IP_MENU.DISP_ORDER
  is '排序号';
comment on column IP_MENU.IS_LEAF
  is '是否末级节点(1-是，0-否)';
comment on column IP_MENU.URL
  is '菜单URL';
comment on column IP_MENU.AUTH_LEVEL
  is '授权级别(0,1,2,3)';
comment on column IP_MENU.IS_SHOW
  is '是否显示(1是,0否)';
comment on column IP_MENU.MENU_DESC
  is '菜单描述';
comment on column IP_MENU.MENU_LOGO
  is '菜单logo的，对应前端的class';
comment on column IP_MENU.PERMISSION
  is 'controller定义的资源权限标识';
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
  is '附件路径';
comment on column IP_NOTICE.NOTICE_TITLE
  is '公告标题';
comment on column IP_NOTICE.ISPUBLISH
  is '是否发布(0-未发布，只保存,1-发布)';
comment on column IP_NOTICE.CREATE_DATE
  is '创建时间';
comment on column IP_NOTICE.UPDATE_DATE
  is '更新时间';
comment on column IP_NOTICE.NOTICE_CONTENT
  is '公告内容';

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
  is '区划ID';
comment on column IP_REGION.THE_CODE
  is '区划编码';
comment on column IP_REGION.THE_NAME
  is '区划名称';
comment on column IP_REGION.PARENT_ID
  is '父级ID';
comment on column IP_REGION.IS_VALID
  is '是否启用区划,0:未启用 1:已启用';
comment on column IP_REGION.CREATE_DATE
  is '创建时间';
comment on column IP_REGION.UPDATE_DATE
  is '更新时间';
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
  is '角色描述';
comment on column IP_ROLE.DISP_ORDER
  is '排序序号';
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
  is '角色id';
comment on column IP_ROLE_SYN.ROLE_CODE
  is '角色code';
comment on column IP_ROLE_SYN.HIRER_ID
  is '租户id';

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
  is '登录名';
comment on column IP_USER.USER_SEX
  is '用户性别 （1男0女）';
comment on column IP_USER.PHONE_NO
  is '电话号码';
comment on column IP_USER.CELLPHONE_NO
  is '手机号码';
comment on column IP_USER.EMPLOYEE_NO
  is '员工号';
comment on column IP_USER.DUTY
  is '职务';
comment on column IP_USER.NATIVE_PLACE
  is '原籍';
comment on column IP_USER.GRADUATE_SCHOOL
  is '毕业学校';
comment on column IP_USER.MAJOR
  is '专业';
comment on column IP_USER.EDUCATION
  is '学历';
comment on column IP_USER.GRADUATIOIN_TIME
  is '毕业时间';
comment on column IP_USER.USER_PIC_URL
  is '用户头像';
comment on column IP_USER.USER_TYPE
  is '用户类型 0：租户管理员  1：普通用户';

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
  is '用户id（关联用户表或租户表）';
comment on column IP_USER_HIDE_MENU.MENU_ID
  is '菜单id，关联菜单表';

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
  is '是否兼职0：不是兼职  1：兼职';
comment on column IP_USER_ROLE.DISP_ORDER
  is '排序序号';
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

