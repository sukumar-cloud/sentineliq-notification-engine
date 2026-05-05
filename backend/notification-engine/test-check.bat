@echo off
echo Checking test files structure...

echo.
echo 1. Checking UserServiceTest.java...
if exist "src\test\java\com\internship\tool\service\UserServiceTest.java" (
    echo [OK] UserServiceTest.java exists
) else (
    echo [MISSING] UserServiceTest.java
)

echo.
echo 2. Checking JwtUtilTest.java...
if exist "src\test\java\com\internship\tool\util\JwtUtilTest.java" (
    echo [OK] JwtUtilTest.java exists
) else (
    echo [MISSING] JwtUtilTest.java
)

echo.
echo 3. Checking GlobalExceptionHandlerTest.java...
if exist "src\test\java\com\internship\tool\exception\GlobalExceptionHandlerTest.java" (
    echo [OK] GlobalExceptionHandlerTest.java exists
) else (
    echo [MISSING] GlobalExceptionHandlerTest.java
)

echo.
echo 4. Checking UserTest.java...
if exist "src\test\java\com\internship\tool\entity\UserTest.java" (
    echo [OK] UserTest.java exists
) else (
    echo [MISSING] UserTest.java
)

echo.
echo 5. Checking test dependencies in pom.xml...
findstr /C:"spring-boot-starter-test" pom.xml >nul
if %errorlevel% equ 0 (
    echo [OK] spring-boot-starter-test dependency found
) else (
    echo [MISSING] spring-boot-starter-test dependency
)

echo.
echo 6. Counting test methods...
findstr /C:"@Test" src\test\java\com\internship\tool\service\UserServiceTest.java | find /V "^" | find /V "import" | find /V "*" | find /C "test" > temp_count.txt
set /p user_tests=<temp_count.txt

findstr /C:"@Test" src\test\java\com\internship\tool\util\JwtUtilTest.java | find /V "^" | find /V "import" | find /V "*" | find /C "test" > temp_count.txt
set /p jwt_tests=<temp_count.txt

findstr /C:"@Test" src\test\java\com\internship\tool\exception\GlobalExceptionHandlerTest.java | find /V "^" | find /V "import" | find /V "*" | find /C "test" > temp_count.txt
set /p exception_tests=<temp_count.txt

findstr /C:"@Test" src\test\java\com\internship\tool\entity\UserTest.java | find /V "^" | find /V "import" | find /V "*" | find /C "test" > temp_count.txt
set /p entity_tests=<temp_count.txt

set /a total_tests=%user_tests%+%jwt_tests%+%exception_tests%+%entity_tests%
echo Total test methods found: %total_tests%

del temp_count.txt

echo.
echo 7. Checking for proper test annotations...
findstr /C:"@SpringBootTest" src\test\java\com\internship\tool\util\JwtUtilTest.java >nul
if %errorlevel% equ 0 (
    echo [OK] @SpringBootTest annotation found
) else (
    echo [WARNING] @SpringBootTest annotation not found
)

findstr /C:"@ExtendWith" src\test\java\com\internship\tool\service\UserServiceTest.java >nul
if %errorlevel% equ 0 (
    echo [OK] @ExtendWith(MockitoExtension.class) annotation found
) else (
    echo [WARNING] @ExtendWith annotation not found
)

echo.
echo Test structure check complete!
echo.
echo To run tests, use:
echo   mvn test
echo or
echo   docker-compose exec app mvn test
pause
