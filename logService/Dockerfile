FROM hub-mirror.c.163.com/library/golang:1.15 AS builder

# 为我们的镜像设置必要的环境变量
ENV GO111MODULE=on \
    CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64 \
    GOPROXY=https://goproxy.cn,direct

# 移动到工作目录：/build
WORKDIR /build

# 将代码复制到容器中
COPY . .
# 将我们的代码编译成二进制可执行文件 log_Service
RUN go build -o log_Service .

###################
# 接下来创建一个小镜像
###################
FROM scratch

# 从builder镜像中把/dist/log_Service 拷贝到当前目录
COPY --from=builder /build/log_Service /

# 需要运行的命令
ENTRYPOINT ["/log_Service"]

EXPOSE 9090
