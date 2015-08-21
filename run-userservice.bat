cd /d %~dp0\user-service
call activator -Dhttp.port=9001 run
pause