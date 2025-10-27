# Giai đoạn 1: Build ứng dụng Java (sử dụng JDK để chạy Maven)
FROM openjdk:21 AS builder

WORKDIR /app

# 1. Sao chép file cấu hình và mã nguồn
# Lý do: Cần file pom.xml và source code để chạy Maven Build
COPY pom.xml .
COPY src ./src

# 2. Chạy Maven Build để tạo file JAR
# Lý do: Đảm bảo file JAR được tạo ra từ source code trong Container
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------------------

# Giai đoạn 2: Runtime (sử dụng JRE nhẹ hơn cho kích thước Image nhỏ)
# *Tôi dùng lại openjdk:21 để giữ tính nhất quán theo yêu cầu của bạn*
FROM openjdk:21

WORKDIR /app

# 3. Sao chép file JAR đã build từ giai đoạn builder
# Giả sử tên file JAR được tạo ra là target/*.jar, ta sao chép nó thành app.jar
COPY --from=builder /app/target/*.jar /app/app.jar

# 4. Cổng tiếp xúc
EXPOSE 8080

# 5. Lệnh khởi chạy ứng dụng
# Lý do: Loại bỏ 'sleep 30' không cần thiết. K8s có cơ chế kiểm tra sự sẵn sàng (Readiness Probes).
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]