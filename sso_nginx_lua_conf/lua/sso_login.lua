local b='ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/' 
-- encoding
function enc(data)
    return ((data:gsub('.', function(x) 
        local r,b='',x:byte()
        for i=8,1,-1 do r=r..(b%2^i-b%2^(i-1)>0 and '1' or '0') end
        return r;
    end)..'0000'):gsub('%d%d%d?%d?%d?%d?', function(x)
        if (#x < 6) then return '' end
        local c=0
        for i=1,6 do c=c+(x:sub(i,i)=='1' and 2^(6-i) or 0) end
        return b:sub(c+1,c+1)
    end)..({ '', '==', '=' })[#data%3+1])
end


function Split(szFullString, szSeparator)
local nFindStartIndex = 1
local nSplitIndex = 1
local nSplitArray = {}
while true do
   local nFindLastIndex = string.find(szFullString, szSeparator, nFindStartIndex)
   if not nFindLastIndex then
    nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, string.len(szFullString))
    break
   end
   nSplitArray[nSplitIndex] = string.sub(szFullString, nFindStartIndex, nFindLastIndex - 1)
   nFindStartIndex = nFindLastIndex + string.len(szSeparator)
   nSplitIndex = nSplitIndex + 1
end
return nSplitArray
end



function  getCallbackUrl( )
  local url =ngx.var.request_uri
  local list=Split(url,"?callbackUrl=")
  local length=#list
  local callbackUrl=''
  if(length~=nil and length==2) then
    callbackUrl='/'..list[length]
  end
return callbackUrl
end




local headerContentType='application/json; charset=utf-8'
ngx.header['Content-Type'] = headerContentType
JSON=require "resty.JSON"
local http = require "resty.http"


--1.先获取请求体中的用户名和密码
local request_type= ngx.var.request_method
--不支持get请求 保护用户信息
if request_type == "GET" then
 return ngx.req.set_uri("/login.html",false)
end
ngx.req.read_body()
local ngxBody = ngx.req.get_body_data()
local requestArray=JSON:decode('['..ngxBody..']')

local userName=requestArray[1]["userName"]
local password=requestArray[1]["password"]

--如果没有获取到用户信息，重新跳到登录页
if(userName==nil or userName=='' or password==nil or password=='')then
  return ngx.req.set_uri("/login.html",false)
end





--2.发起sso登录请求
local authUrl="http://127.0.0.1:10084/api/sso/ssoLogin"
--制定请求头内容
local authBody='{"userName": "'..userName..'","password": "'..password..'"}'
--判断请求状态
local sendStatus=0
--发起后台请求接口查看
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

--3.如果调用sso接口服务异常，则直接返回到登录页面
if(sendStatus==1) then
 return ngx.req.set_uri("/login.html", false)
  
end


--4.解析sso接口返回的json数据
local authResponseTable = JSON:decode('['..restr..']')
local code=authResponseTable[1]["code"]

--5.如果确认用户校验成功，则开始将数据加密处理后写入cookie
if(code~=nil and code==10000)then
  local data= authResponseTable[1]["data"]
  data=JSON:encode(data)
  data=JSON:decode('['..data..']')

  local ut=data[1]["userToken"]
  local uit=data[1]["userInfoDTO"]
  uit=JSON:encode(uit)
  --加密userInfo信息
  uit=enc(uit)
  local cookie_val='ut='..ut..';uit='..uit
   --写入cookie 默认时间30分钟 只允许http请求方式访问
  ngx.header["Set-Cookie"] = {
        "ut=" .. ut .. "; Max-Age=108000;Domain=localhost;Path=/; HttpOnly ",
        "uit=" .. uit .. "; Max-Age=108000;Domain=localhost;Path=/; HttpOnly"
    }
  --6.如果有指定回调地址，则直接跳转到回调地址
    local needReturnUrl=getCallbackUrl()
    if(needReturnUrl~=nil and needReturnUrl~='') then
      return ngx.req.set_uri(needReturnUrl,false)
    end
  --7.默认跳转到首页
   return ngx.req.set_uri("/ming_index.html", false)
else
     return ngx.req.set_uri("/loginFail.html", false)

end




