# Tetris
__試作のデモテトリス__ (libGDX 版)

`game/` 以下は [libGDX](https://libgdx.com/) を使ったマルチモジュール Gradle プロジェクトです。

| モジュール | 内容 |
| --- | --- |
| `core` | ゲーム本体 (画面・ロジック)。プラットフォーム非依存 |
| `lwjgl3` | デスクトップ (Windows / macOS / Linux) 用ランチャー |
| `assets` | 画像などのリソース |

*実行方法*
* `cd game && ./gradlew lwjgl3:run`

*実行可能 JAR の作成*
* `cd game && ./gradlew lwjgl3:dist` → `game/lwjgl3/build/libs/tetris-all.jar`
* `java -jar tetris-all.jar` で起動

*テスト*
* `cd game && ./gradlew test`

Java 25 のツールチェーンを使います。別のバージョンでビルドする場合は `-PjavaVersion=21` のように指定してください。

*操作*
* ← / → : 移動
* ↓ : ソフトドロップ (押している間落下速度 2 倍)
* G : 右回転 / F : 左回転

*C# 版*
* Tetris.exe(C#で書き直したバージョン)をダブルクリック
