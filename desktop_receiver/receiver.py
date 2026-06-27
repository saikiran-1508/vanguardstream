"""
VanguardStream Desktop Receiver
Listens for incoming JPEG frames from the Android app and displays them live.

Usage:
    python receiver.py [--port PORT]

Requirements:
    pip install opencv-python numpy
"""

import socket
import struct
import sys
import argparse
import cv2
import numpy as np


def recv_all(conn: socket.socket, length: int) -> bytes | None:
    """Read exactly `length` bytes from the socket, or return None on disconnect."""
    data = b""
    while len(data) < length:
        chunk = conn.recv(length - len(data))
        if not chunk:
            return None
        data += chunk
    return data


def run_receiver(host: str = "0.0.0.0", port: int = 8554) -> None:
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind((host, port))
    server.listen(1)

    print(f"[VanguardStream] Listening on {host}:{port}")
    print("[VanguardStream] Waiting for Android device to connect...")
    print("                 Press 'q' in the video window to quit.\n")

    conn, addr = server.accept()
    print(f"[VanguardStream] Connected: {addr[0]}:{addr[1]}")

    frame_count = 0
    try:
        while True:
            # Each frame is prefixed with a 4-byte big-endian length
            raw_len = recv_all(conn, 4)
            if raw_len is None:
                print("\n[VanguardStream] Android device disconnected.")
                break

            frame_len = struct.unpack(">I", raw_len)[0]
            jpeg_data = recv_all(conn, frame_len)
            if jpeg_data is None:
                print("\n[VanguardStream] Stream interrupted.")
                break

            frame = cv2.imdecode(
                np.frombuffer(jpeg_data, dtype=np.uint8),
                cv2.IMREAD_COLOR,
            )
            if frame is not None:
                frame_count += 1
                cv2.imshow("VanguardStream", frame)

            if cv2.waitKey(1) & 0xFF == ord("q"):
                print("\n[VanguardStream] Quit requested.")
                break
    finally:
        conn.close()
        server.close()
        cv2.destroyAllWindows()
        print(f"[VanguardStream] Session ended. Frames received: {frame_count}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="VanguardStream desktop receiver")
    parser.add_argument("--port", type=int, default=8554, help="Port to listen on (default: 8554)")
    args = parser.parse_args()
    run_receiver(port=args.port)
