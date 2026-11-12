# spooler stop
net stop "Spooler"

#spool file delete
forfiles /P C:\Windows\System32\spool\PRINTERS /S  /C "cmd /c del @file"


#spooler start
net start "Spooler"
