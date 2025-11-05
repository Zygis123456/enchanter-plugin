@echo off
echo =====================================
echo        Enchanter Plugin Builder
echo =====================================
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven nerastas! Isikelkite Maven is https://maven.apache.org/
    echo.
    pause
    exit /b 1
)

echo Maven rastas! Kompiliuojamas pluginas...
echo.

REM Clean and compile
echo [1/3] Valomas projektas...
call mvn clean
if %errorlevel% neq 0 (
    echo ERROR: Nepavyko isvaldyti projekto!
    pause
    exit /b 1
)

echo.
echo [2/3] Kompiliuojamas kodas...
call mvn compile
if %errorlevel% neq 0 (
    echo ERROR: Nepavyko sukompiliuoti kodo!
    pause
    exit /b 1
)

echo.
echo [3/3] Kuriamas JAR failas...
call mvn package
if %errorlevel% neq 0 (
    echo ERROR: Nepavyko sukurti JAR failo!
    pause
    exit /b 1
)

echo.
echo =====================================
echo        KOMPILIAVIMAS SĖKMINGAS!
echo =====================================
echo.

REM Check if jar file exists
if exist "target\Enchanter-1.0.0.jar" (
    echo JAR failas sukurtas: target\Enchanter-1.0.0.jar
    echo Failo dydis:
    for %%A in ("target\Enchanter-1.0.0.jar") do echo   %%~zA baitu ^(%%~zA KB^)
    echo.
    echo Galite nukopijuoti faila i savo serverio plugins direktorija.
    echo.
    
    REM Ask if user wants to copy to plugins directory
    set /p copy_choice="Ar norite nukopijuoti i plugins direktorija? (Y/N): "
    if /i "%copy_choice%"=="Y" (
        if not exist "plugins" mkdir plugins
        copy "target\Enchanter-1.0.0.jar" "plugins\"
        echo Failas nukopijuotas i plugins direktorija!
    )
) else (
    echo WARNING: JAR failas nerastas! Patikrinkite klaidas.
)

echo.
echo Enchantmentai, kurie bus galimi:
echo - Unbreakable (Daikto tvarumas)
echo - Explosive (Sprogimai)
echo - Lifesteal (Gyvybės vagystė)
echo - AutoRepair (Automatinis taisymas)
echo - Lightning (Žaibai)
echo - Teleport (Teleportacija)
echo - Freezing (Užšaldymas)
echo - Poison (Nuodai)
echo.
echo Komandos:
echo - /enchant ^<enchantmentas^> [lygis] [žaidėjas]
echo - /listenchants (sąrašas enchantmentų)
echo - /enchanterreload (perkrauti konfigūraciją)
echo.
pause