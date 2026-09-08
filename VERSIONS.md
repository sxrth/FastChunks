# FastChunks version matrix

Requested targets:
1.21, 1.21.1, 1.21.2, 1.21.3, 1.21.4, 1.21.5, 1.21.6,
1.21.7, 1.21.8, 1.21.9, 1.21.10, 1.21.11, 26.1, 26.1.1, 26.1.2, 26.2.

Each target is a separate JAR. The workflow selects Java 21 for 1.21.x and
Java 25 for 26.x. Fabric documents the Loom plugin split at 26.1 and recommends
Loom 1.17 + Gradle 9.5.1 for 26.2.
