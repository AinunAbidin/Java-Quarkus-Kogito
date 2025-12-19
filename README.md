# code-quarkus-kogito

Proyek ini berisi contoh “simple checkout” berbasis Kogito BPMN:

- Proses `simple_checkout` di `src/main/resources/simple-checkout.bpmn2` dengan alur Start → Validate → Calculate Total → Create Order → End.
- Model: `org.acme.checkout.CheckoutData` (userId, cartId, items, total, orderId, status).
- Layanan: `org.acme.checkout.service.CheckoutService` (validate, calculate, createOrder).
- REST: `POST /checkout` menerima JSON CheckoutData dan mengembalikan hasil proses.
- testing hanya kondisi success: `src/test/java/org/acme/checkout/SimpleCheckoutProcessTest.java`.

## Stack singkat
- Java 17, Quarkus 3.15, Kogito 10.1 (process engine) kogito tidak sepenuhnya compatible dengan quarkus terbarus karena library yang sudah usang ini dilakukan dengan metode force install banyak feature tambahan kogito tidak berjalan maksimal seperti menampilkan diagram lewat quarkus.  
- Kogito DevServices dimatikan karena buat error karena pull image otomatis dalam case ini kita tidak menggunakan database karena ada point2 yang harus diutamkan.

## Alur & service
- Start → Validate → Calculate Total → Create Order → End.
- Validate: cek userId/cartId ada atau tidak items tidak kosong, qty > 0, price ≥ 0; kalau invalid lempar `IllegalArgumentException`.
- Calculate Total: total = sum(qty * price) untuk semua item.
- Create Order: set `orderId` format `ORD-<UUID>` dan `status` = `CREATED`.

## Instalasi
1) Install JDK 17, pastikan `java -version` 17.
2) Unduh deps & tes: `./mvnw test` (pastikan hijau).
3) gambar svg kogito ada di `./src/main/resources/META-INF/processSVG/simple-checkout.svg` kalau ingin merubah dan generate ulang pastikan ada mempunyai tools atau extension vscode untuk mengedit file bpmn 
4) untuk mengunjungi quarkus bisa ke `http://localhost:8080/q/dev-ui/extensions` namun hanya bisa diakses jika menggunakan mode dev
    tidak bisa diakses jika menjalanka menggunakan docker build image
4) Dev mode: `./mvnw quarkus:dev`
   - Endpoint bentuk : `POST http://localhost:8080/checkout` dengan body:
     ```json
     {
       "userId": "user-123",
       "cartId": "cart-123",
       "items": [
         { "name": "Item-1", "qty": 2, "price": 10000 },
         { "name": "Item-2", "qty": 1, "price": 25000 }
       ]
     }
     ```
   - Contoh curl:
     ```bash
     curl -X POST http://localhost:8080/checkout \
       -H "Content-Type: application/json" \
       -d '{
         "userId": "user-123",
         "cartId": "cart-123",
         "items": [
           { "name": "Item-1", "qty": 2, "price": 10000 },
           { "name": "Item-2", "qty": 1, "price": 25000 }
         ]
       }'
     ```
   - Respons berisi `total`, `orderId`, `status`.
5) hasil ouput akan seperti ini
```json
{
    "userId": "user-123",
    "cartId": "cart-123",
    "items": [
        {
            "name": "Item-1",
            "qty": 2,
            "price": 10000
        },
        {
            "name": "Item-2",
            "qty": 1,
            "price": 25000
        }
    ],
    "total": 45000,
    "orderId": "ORD-2aa8fca7-d6f7-4cb8-b1b6-73fdf1281752",
    "status": "CREATED"
}
```

## Catatan diagram/visual
- Ekstensi process-SVG untuk Kogito 10.x belum tersedia di Maven Central; gunakan file BPMN langsung dengan viewer/editor BPMN (mis. extension BPMN/Kogito di VS Code) untuk melihat diagram.

## Build
- Unit test: `./mvnw test`
- Package jar: `./mvnw package`

## Run via Docker (opsional)
1) Build app: `./mvnw package -DskipTests`
2) Build image (mode JVM):  
   `docker build -f src/main/docker/Dockerfile.jvm -t checkout-app .`
3) Jalankan:  
   `docker run -it --rm -p 8080:8080 checkout-app`

Endpoint tetap di `http://localhost:8080/checkout`
