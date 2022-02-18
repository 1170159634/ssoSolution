local b='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/' 

-- 解密
function dec(data)
    data = string.gsub(data, '[^'..b..'=]', '')
    return (data:gsub('.', function(x)
        if (x == '=') then return '' end
        local r,f='',(b:find(x)-1)
        for i=6,1,-1 do r=r..(f%2^i-f%2^(i-1)>0 and '1' or '0') end
        return r;
    end):gsub('%d%d%d?%d?%d?%d?%d?%d?', function(x)
        if (#x ~= 8) then return '' end
        local c=0
        for i=1,8 do c=c+(x:sub(i,i)=='1' and 2^(8-i) or 0) end
            return string.char(c)
    end))
end

local headerContentType='application/json; charset=utf-8'
ngx.header['Content-Type'] = headerContentType
local defaultResult='{"code":10001,"msg":"用户校验失败，请重新登录"}'
local result = ""


JSON=require "resty.JSON"
--1.获取请求头中的cookie 其中包含的token ut=?;uit=?	如果没有获取到相应的token，则直接返回失败
local headers_tab = ngx.req.get_headers()
local userToken=""
local userInfoToken=""
local utIsExist=0
local uitIsExist=0

for key, value in pairs(headers_tab) do
  if(key=="cookie") then
     if(value~=nil)then
		local length=#value
        local d,f=string.find(value,"ut=")
        if(d~=nil or f~=nil)then
        	utIsExist=1
       		userToken=value.sub(value,f+1,length)
       		local z,x=string.find(userToken,";")

        	if(z~=nil or x~=nil)then
			userToken=userToken.sub(userToken,0,x-1)
			end
        end

        local n,m=string.find(value,"uit=")
        if(n~=nil or m~=nil)then
        	uitIsExist=1
       		userInfoToken=value.sub(value,m+1,length)
       		local z,x=string.find(userInfoToken,";")

        	if(z~=nil or x~=nil)then
			userInfoToken=userInfoToken.sub(userInfoToken,0,x-1)
			end
        end


     end
     break

  end
end

if(utIsExist == 0 or uitIsExist == 0 ) then
ngx.say(defaultResult)
return
end


--2.解密其中的uit，取出uid
local decryptData=dec(userInfoToken)
local uid
local uidisExist=0
local uidStartIndex,uidEndIndex=string.find(decryptData,"userId")

if(uidStartIndex~=nil and uidEndIndex~=nil)then
local decryptDataArray=JSON:decode('['..decryptData..']')
uid=decryptDataArray[1]["userId"]
if(uid~=nil and uid~='' and uid~='nil')then
uidisExist=1
end
end


--如果解密失败或者没有获取到需要的的userId信息，直接返回校验失败
if(uidisExist==0) then
ngx.say('{"code":10004,"msg":"字段校验错误"}')
return
end



--3.发起鉴权请求，将token跟uid传递
local authUrl="http://127.0.0.1:10084/api/sso/checkLogin"
--制定请求头内容
local authBody='{"userToken": "'..userToken..'","userId": "'..uid..'"}'
--判断请求状态
local sendStatus=0
--发起后台请求接口查看
local http = require "resty.http"
local httpc = http.new()
local res, err = httpc:request_uri(authUrl, {
    method = "POST",
    body = authBody,
    headers = {
        ["Content-Type"] = headerContentType,
    }
})
if not res then
    sendStatus=1
end

ngx.status = res.status
if ngx.status ~= 200 then
    sendStatus=1
end


local restr = res.body

--4.如果调用sso接口服务异常，则直接返回
if(sendStatus==1) then
  result='{"code":10003,"msg":"系统未知异常"}'
  ngx.say(result)
  return
end


--5.解析服务返回的json数据
local authResponseTable = JSON:decode('['..restr..']')
local code=authResponseTable[1]["code"]



--6.如果校验成功，返回用户信息
if(code~=nil and code==10000)then
  result='{"code":10000,"msg":"用户校验成功","data": ' .. decryptData .. '}'

  ngx.say(result)
  return
end

--7.如果sso接口其他信息，默认为校验失败，直接return
ngx.say(defaultResult)



