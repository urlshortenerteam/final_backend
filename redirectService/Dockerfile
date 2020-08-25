FROM golang:1.15 AS builder

# 为我们的镜像设置必要的环境变量
ENV GO111MODULE=on \
    CGO_ENABLED=1 \
    GOOS=linux \
    GOARCH=amd64 \
    GOPROXY=https://goproxy.cn,direct

# 移动到工作目录：/build
WORKDIR /build

# 将代码复制到容器中
COPY . .
# 将我们的代码编译成二进制可执行文件 redirect_Service
RUN go build --ldflags "-extldflags -static" -o redirect_Service .

###################
# 接下来创建一个小镜像
###################
FROM busybox:glibc

# 从builder镜像中把/dist/redirect_Service 拷贝到当前目录
COPY --from=builder /build/redirect_Service /
COPY credentials.env .

# 需要运行的命令
ENTRYPOINT ["/redirect_Service"]

EXPOSE 9090
