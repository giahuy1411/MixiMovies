$files = @(
    "admin.jsp",
    "login.jsp",
    "register.jsp",
    "forgot-password.jsp",
    "reset-password.jsp",
    "verify-otp.jsp"
)

foreach ($file in $files) {
    $path = "c:\Users\ASUS\Documents\LAPTRINH\JAVAWEB2\MixiMovies\src\main\webapp\views\$file"
    if (Test-Path $path) {
        $content = Get-Content -Path $path -Raw
        # Replace only if it doesn't already have csrf_token close by
        $pattern = '(?i)(<form[^>]*method=["'']post["''][^>]*>)'
        $replacement = "`$1`r`n    <input type=`"hidden`" name=`"csrf_token`" value=`"`${sessionScope.csrf_token}`">"
        
        $newContent = [regex]::Replace($content, $pattern, $replacement)
        
        # Save it
        [IO.File]::WriteAllText($path, $newContent, [System.Text.Encoding]::UTF8)
        Write-Host "Updated $file"
    } else {
        Write-Host "Skipped $file (not found)"
    }
}
