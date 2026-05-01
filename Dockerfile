# Stage 1: Build (No changes here)
FROM ghcr.io/graalvm/native-image-community:21 AS build
WORKDIR /build
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests
RUN ./mvnw -Pnative package -pl api/spring -am -DskipTests

# Stage 2: Final Image
# CHANGE: Switch from 'base-debian12' to 'cc-debian12'
# Stage 2: Final Image
FROM gcr.io/distroless/cc-debian12

WORKDIR /app

COPY --from=build /build/api/spring/target/order-service /app/order-service

ENTRYPOINT ["/app/order-service"]