# CursebyteCore 🛡️

**CursebyteCore** adalah plugin inti (Core) kustom yang dirancang untuk server Minecraft Roleplay berbasis **Paper (1.21+)**. Plugin ini menangani sistem fundamental server seperti Kewarganegaraan (Imigrasi), Ekonomi, dan Database Management dengan pendekatan *State Management* yang efisien.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Platform](https://img.shields.io/badge/Platform-PaperMC-blue)
![Status](https://img.shields.io/badge/Status-Development-green)

## ✨ Fitur Utama

### 1. 🛂 Sistem Imigrasi (Citizenship)
Sistem pendaftaran wajib bagi pemain baru untuk menjaga kualitas Roleplay.
- **Isolasi Player Baru:** Player baru otomatis dipindahkan ke ruang imigrasi dan tidak bisa chat/command sebelum mendaftar.
- **Interaksi NPC Cerdas:** Integrasi dengan **Citizens API** untuk NPC Petugas (Villager Librarian) yang interaktif.
- **Input Chat State:** Player memasukkan nama asli (Roleplay Name) langsung di chat tanpa mengganggu chat global.
- **Identity Generation:** Otomatis membuat **NIK** unik (Format: `YYYYMMDD-RND`) dan memberikan item fisik **KTP** yang tersimpan di database.

### 2. 💸 Sistem Ekonomi & Transaksi
Sistem perbankan yang aman dan *user-friendly*.
- **Direct Chat Transfer:** Input nominal transfer langsung via chat (tanpa ribet GUI Anvil/Sign).
- **Transaction Logging:** Setiap transaksi (Masuk/Keluar) dicatat di database MySQL/SQLite untuk audit log.
- **Async Database Handling:** Operasi berat database dijalankan di *background thread* (Async) untuk menjaga TPS server tetap stabil (20 TPS).
- **Visual Feedback:** Notifikasi menggunakan **Action Bar** dan **Sound Effect** modern.

### 3. 🛠️ Dynamic Configuration
Semua lokasi penting dapat diatur langsung dari dalam game tanpa menyentuh kode.
- Lokasi Spawn Imigrasi.
- Lokasi dan Nama NPC Petugas.

---

## 📋 Prasyarat (Requirements)

Sebelum menjalankan plugin ini, pastikan server memiliki:
1.  **Java 17** atau lebih baru.
2.  **Paper 1.21.4** (atau versi yang kompatibel).
3.  **[Citizens](https://ci.citizensnpcs.co/job/Citizens2/)** (Wajib install untuk fitur NPC).

---

## ⚙️ Instalasi & Setup

1.  Download file `.jar` terbaru dari menu Releases.
2.  Masukkan ke folder `plugins/` di server Anda.
3.  Pastikan plugin **Citizens** sudah terinstall.
4.  Restart server.
5.  Lakukan setup in-game (lihat Command di bawah).

---

## ⌨️ Commands & Permissions

| Command | Permission | Deskripsi |
| :--- | :--- | :--- |
| `/setupimigrasi spawn` | `imigrasi.admin` | Mengatur titik spawn player baru di ruang imigrasi. |
| `/setupimigrasi npc` | `imigrasi.admin` | Menyimpan lokasi berdiri admin sebagai lokasi NPC Petugas. |

> **Catatan:** Setelah mengatur lokasi NPC, restart server atau reload plugin untuk memunculkan NPC secara fisik.

---

## 🏗️ Cara Build (Untuk Developer)

Project ini menggunakan **Maven**. Namun, karena project ini mengakses NMS (Net Minecraft Server) secara langsung, Anda perlu menyediakan file server secara manual.

1.  Clone repository ini.
2.  Dapatkan file `paper-1.21.4-mojang-mapped.jar` (Generate dari server Paper).
3.  Edit `pom.xml`, sesuaikan path pada bagian `systemPath`:
    ```xml
    <dependency>
        <groupId>io.papermc.paper</groupId>
        <artifactId>paper-server-manual</artifactId>
        <version>1.21.4</version>
        <scope>system</scope>
        <systemPath>C:/Path/To/Your/paper-mojang-mapped.jar</systemPath>
    </dependency>
    ```
4.  Jalankan perintah build:
    ```bash
    mvn clean package
    ```

---

## 📂 Konfigurasi (`config.yml`)

```yaml
immigration:
  spawn-location: 
    world: world
    x: 100.5
    y: 64.0
    z: -250.5
    yaw: 90.0
    pitch: 0.0
  npc:
    id: 12
    name: "&ePetugas Layanan"
    location:
      world: world
      x: 105.5
      y: 64.0
      z: -245.5