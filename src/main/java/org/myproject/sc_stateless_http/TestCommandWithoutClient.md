# Test HTTP server without client program

## Test HTTP /start endpoint
- User goes first
```bash
curl.exe -X POST http://localhost:8080/start -H "Content-Type: application/json" -d '{"turn": 1}'
```
or
```bash
$body = @{
    turn = 1
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "http://localhost:8080/start" `
  -Method Post `
  -ContentType "application/json" `
  -Body $body
```
- User goes second
```bash
curl.exe -X POST http://localhost:8080/start -H "Content-Type: application/json" -d '{"turn": 2}'
```
or
```bash
$body = @{
    turn = 2
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "http://localhost:8080/start" `
  -Method Post `
  -ContentType "application/json" `
  -Body $body
```

## Test HTTP /move endpoint
```bash
$body = @{
    turn = 2
    move = 8
    board_msg = "1,2,1,1,2,0,0,0,0"
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri "http://localhost:8080/move" `
  -Method Post `
  -ContentType "application/json" `
  -Body $body
```