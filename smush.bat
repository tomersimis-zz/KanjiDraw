@ECHO OFF
SETLOCAL enabledelayedexpansion
 
set "convert=C:\Program Files\ImageMagick-6.9.1-Q8\mogrify.exe"
set "identify=C:\Program Files\ImageMagick-6.9.1-Q8\identify.exe"
 
rem cd resources\images
cd app
FOR /R "." %%G in (*.png) DO (
    call :subroutine "%%G"
)
rem cd ..\..
cd ..
goto :EOF
 
:subroutine
  set "var=%1"
  set "tmp=%1_c.png"
  rem echo %var%
  if not x%var:src=%==x%var% (
    echo Changing %var%
    rem %identify% %var%
    echo %convert% -strip %var%
  )
  goto :EOF
 
EOF: