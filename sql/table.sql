-- Table: tts_server

-- DROP TABLE tts_server;

CREATE TABLE tts_server
(
  id serial NOT NULL,
  ip character varying NOT NULL,
  type integer default 1, -- 1:本地机房 2:远程机房
  active integer default 1, -- 是否激活 0:不激活 1:激活
  license integer, -- license个数
  vid integer, -- 语音库 1，普通话 2，粤语 3，普通话+粤语
  create_time timestamp with time zone DEFAULT now(), -- 记录创建时间
  CONSTRAINT tts_server_pkey PRIMARY KEY (id)
)
WITHOUT OIDS;
ALTER TABLE tts_server OWNER TO postgres;
COMMENT ON TABLE tts_server IS 'tts服务器列表';
COMMENT ON COLUMN tts_server.id IS 'id标识';
COMMENT ON COLUMN tts_server.ip IS 'ip地址';
COMMENT ON COLUMN tts_server.type IS '1:本地机房 2:远程机房';
COMMENT ON COLUMN tts_server.active IS '是否激活 0:不激活 1:激活';
COMMENT ON COLUMN tts_server.license IS 'license个数';
COMMENT ON COLUMN tts_server.vid IS '语音库 1，普通话 2，粤语 3，普通话+粤语';
COMMENT ON COLUMN tts_server.create_time IS '记录创建时间';

-- Table: system_setting

-- DROP TABLE system_setting;

CREATE TABLE system_setting
(
  id serial NOT NULL, -- id标识
  name character varying, -- name-value对
  value character varying, -- name-value对
  property character varying, -- 属性
  create_time timestamp with time zone DEFAULT now(), -- 记录创建时间
  CONSTRAINT system_pkey PRIMARY KEY (id)
) 
WITHOUT OIDS;
ALTER TABLE system_setting OWNER TO postgres;
COMMENT ON TABLE system_setting IS '系统设置表';
COMMENT ON COLUMN system_setting.id IS 'id标识';
COMMENT ON COLUMN system_setting.name IS 'name-value对';
COMMENT ON COLUMN system_setting.value IS 'name-value对';
COMMENT ON COLUMN system_setting.property IS '属性';
COMMENT ON COLUMN system_setting.create_time IS '记录创建时间';


-- Table: tts_log

-- DROP TABLE tts_log;

CREATE TABLE tts_log
(
  id serial NOT NULL, -- id标识
  enterprise_id character varying, -- 企业id，空为非企业调用
  priority integer default 3, -- 请求的优先级 1-10，优先级越高越优先，默认是3
  unique_id character varying, -- 一次文本的唯一标识
  uuid character varying, -- 请求的唯一标识 使用绝对唯一标识，同一uuid的不同记录是重试
  text character varying, -- 文本片段，多个片段可能对应一个uniqueId
  hash character varying, -- 文本片段的hash值
  request_time timestamp with time zone DEFAULT now(), -- 记录请求时间
  start_time timestamp with time zone DEFAULT now(), -- 记录开始转换时间
  end_time timestamp with time zone DEFAULT now(), -- 记录转换完成时间
  position integer, -- 进入排队的位置
  hit_cache integer, -- 是否命中缓存 0:未命中 1:命中
  result integer, -- 合成结果 0:成功 其他失败
  tts_server_ip character varying, -- 转换用的tts_server ip地址
  tts_server_type integer, -- 转换用tts_server的类型
  thread_id integer, -- 转换用的thread id
  from_ip character varying, -- 请求来源ip
  speed integer, -- 播放速度 -20到20之间 默认0
  vid integer, -- 语音库 0普通话 3粤语
  volume integer, -- 语音音量
  redirect integer, 
  sync integer,
  create_time timestamp with time zone DEFAULT now(), -- 记录创建时间
  CONSTRAINT tts_log_pkey PRIMARY KEY (id)
) 
WITHOUT OIDS;
ALTER TABLE tts_log OWNER TO postgres;
COMMENT ON TABLE tts_log IS 'tts日志表';
COMMENT ON COLUMN tts_log.id IS 'id标识';
COMMENT ON COLUMN tts_log.enterprise_id IS '企业id，空为非企业调用';
COMMENT ON COLUMN tts_log.priority IS '请求的优先级 1-10，优先级越高越优先，默认是3';
COMMENT ON COLUMN tts_log.unique_id IS '一次文本的唯一标识';
COMMENT ON COLUMN tts_log.uuid IS '请求的唯一标识 使用绝对唯一标识，同一uuid的不同记录是重试';
COMMENT ON COLUMN tts_log.text IS '文本片段，多个片段可能对应一个uniqueId';
COMMENT ON COLUMN tts_log.hash IS '文本片段的hash值';
COMMENT ON COLUMN tts_log.request_time IS '记录请求时间';
COMMENT ON COLUMN tts_log.start_time IS '记录开始转换时间';
COMMENT ON COLUMN tts_log.end_time IS '记录转换完成时间';
COMMENT ON COLUMN tts_log.position IS '进入排队的位置';
COMMENT ON COLUMN tts_log.hit_cache IS '是否命中缓存 0:未命中 1:命中';
COMMENT ON COLUMN tts_log.result IS '合成结果 0:成功 其他失败';
COMMENT ON COLUMN tts_log.tts_server_ip IS '转换用的tts_server ip地址';
COMMENT ON COLUMN tts_log.tts_server_type IS '转换用tts_server的类型';
COMMENT ON COLUMN tts_log.thread_id IS '转换用的thread id';
COMMENT ON COLUMN tts_log.from_ip IS '请求来源ip';
COMMENT ON COLUMN tts_log.speed IS '播放速度 -20到20之间 默认0';
COMMENT ON COLUMN tts_log.vid IS '语音库 0普通话 3粤语';
COMMENT ON COLUMN tts_log.volume IS '语音音量 -20到+20 默认10';
COMMENT ON COLUMN tts_log.redirect IS '是否跳转wav路径';
COMMENT ON COLUMN tts_log.sync IS '是否同步调用';
COMMENT ON COLUMN tts_log.create_time IS '记录创建时间';

-- Index: tts_log_enterprise_id_index

-- DROP INDEX tts_log_enterprise_id_index;

CREATE INDEX tts_log_enterprise_id_index
  ON tts_log
  USING btree
  (enterprise_id );

-- Index: tts_log_unique_id_index

-- DROP INDEX tts_log_unique_id_index;

CREATE INDEX tts_log_unique_id_index
  ON tts_log
  USING btree
  (unique_id );
-- Index: tts_log_request_time_index

-- DROP INDEX tts_log_request_time_index;

CREATE INDEX tts_log_request_time_index
  ON tts_log
  USING btree
  (request_time );

-- Index: tts_log_tts_server_ip_index

-- DROP INDEX tts_log_tts_server_ip_index;

CREATE INDEX tts_log_tts_server_ip_index
  ON tts_log
  USING btree
  (tts_server_ip );


-- Index: tts_log_thread_id_index

-- DROP INDEX tts_log_thread_id_index;

CREATE INDEX tts_log_thread_id_index
  ON tts_log
  USING btree
  (thread_id );

-- Table: tts_monitor

-- DROP TABLE tts_monitor;

CREATE TABLE tts_monitor
(
  id serial NOT NULL, -- id标识
  total_engine_count integer, -- 总thread数
  used_engine_count integer, -- 已使用数
  wait_count integer, -- 排队个数
  deal_count integer, --已处理个数
  fail_count integer, --失败个数
  engine_info json, --engines监控信息
  create_time timestamp with time zone DEFAULT now(), -- 记录创建时间
  CONSTRAINT tts_monitor_pkey PRIMARY KEY (id)
) 
WITHOUT OIDS;
ALTER TABLE tts_monitor OWNER TO postgres;
COMMENT ON TABLE tts_monitor IS 'tts监控日志';
COMMENT ON COLUMN tts_monitor.id IS 'id标识';
COMMENT ON COLUMN tts_monitor.total_engine_count IS '总thread数';
COMMENT ON COLUMN tts_monitor.used_engine_count IS '已使用数';
COMMENT ON COLUMN tts_monitor.wait_count IS '排队个数';
COMMENT ON COLUMN tts_monitor.deal_count IS '已处理个数';
COMMENT ON COLUMN tts_monitor.fail_count IS '失败个数';
COMMENT ON COLUMN tts_monitor.engine_info IS 'engines监控信息';
COMMENT ON COLUMN tts_monitor.create_time IS '记录创建时间';


