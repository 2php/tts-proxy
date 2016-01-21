-- 添加系统设置
insert into system_setting(name,value,property) values('tts_cache_abs_path','/var/nfs/tts_cache',''); -- 缓存存放目录
insert into system_setting(name,value,property) values('tts_cache_path','/tts_cache',''); -- 缓存相对web目录
insert into system_setting(name,value,property) values('default_timeout','10',''); -- 默认超时时间
insert into system_setting(name,value,property) values('default_priority','6',''); -- 默认优先级
insert into system_setting(name,value,property) values('alarm_email','vocp_list@ti-net.com.cn','10');  --提醒邮件 每10分钟发一次
insert into system_setting(name,value,property) values('admin','admin','123456');  -- 管理员账号密码
insert into system_setting(name,value,property) values('admin','anjb','123456');  -- 管理员账号密码 可以有多个管理员
insert into system_setting(name,value,property) values('monitor_conf', '30', '7'); -- 监控设置 value中为监控间隔秒数，默认10秒，property中为保存表天数默认7天
insert into system_setting(name,value,property) values('s3_bucket', 'tinet-tts-cache', ''); -- s3上的桶名
insert into system_setting(name,value,property) values('aws_credential', '', ''); -- aws accessKey & aws secretKey
