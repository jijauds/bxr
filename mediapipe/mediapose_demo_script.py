import cv2
import mediapipe as mp
import numpy as np
import csv
import dataclasses
import drawing_utils as mp_drawing
from typing import List, Mapping, Optional, Tuple, Union

mp_pose = mp.solutions.pose
pose = mp_pose.Pose(static_image_mode=False,
                    model_complexity=2,
                    enable_segmentation=False,
                    min_detection_confidence=0.5,
                    min_tracking_confidence=0)
#mp_drawing = drawing_utils

def angle_between(a, b, c):
    a, b, c = np.array(a), np.array(b), np.array(c)
    ba, bc = a - b, c - b
    cos_angle = np.dot(ba, bc) / (np.linalg.norm(ba)*np.linalg.norm(bc)+1e-8)
    return np.degrees(np.arccos(np.clip(cos_angle, -1.0, 1.0)))

def put_angle(frame, angle, point, name, color=(255,0,255)):
    cv2.putText(frame, f"{name}:{int(angle)}",
                (int(point[0]), int(point[1]) - 10),
                cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

input_video = "../test videos/SLOW 2.mp4"  
output_video = "leg hip.mp4"
output_csv   = "VID20251013031338.csv"

cap = cv2.VideoCapture(input_video)
fourcc = cv2.VideoWriter_fourcc(*'mp4v')
fps = cap.get(cv2.CAP_PROP_FPS)
w, h = int(cap.get(3)), int(cap.get(4))
outv = cv2.VideoWriter(output_video, fourcc, fps, (w,h))

csv_file = open(output_csv, "w", newline="")
csv_writer = csv.writer(csv_file)
header = ["frame", "L_Elbow", "R_Elbow", "L_Knee", "R_Knee",
          "L_Shoulder", "R_Shoulder", "L_Hip", "R_Hip"]
csv_writer.writerow(header)

frame_idx = 0

@dataclasses.dataclass
class DrawingSpec:
  # Color for drawing the annotation. Default to the white color.
  color: Tuple[int, int, int] = (0,0,0)
  # Thickness for drawing the annotation. Default to 2 pixels.
  thickness: int = 2
  # Circle radius. Default to 2 pixels.
  circle_radius: int = 2

while True:
    ret, frame = cap.read()
    if not ret: break

    rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    result = pose.process(rgb)

    #print(mp_pose.POSE_CONNECTIONS)

    angles_row = [frame_idx] + [None]*10 

    connects = frozenset([(12,11),(12,14),(14,16),(11,13),(13,15),(12,24),(11,23),(24,23),(24,26),(23,25),(26,30),(25,29)])

    if result.pose_landmarks:
        triplets = {
            "L_Hand": (11,15,12),
            "R_Hand": (12,16,11),
            "L_Elbow":  (11, 13, 15),
            "R_Elbow":  (12, 14, 16),
            "L_Knee":   (23, 25, 27),
            "R_Knee":   (24, 26, 28),
            "L_Shoulder": (13, 11, 23),
            "R_Shoulder": (14, 12, 24),
            "L_Hip":    (24, 23, 25),
            "R_Hip":    (23, 24, 26),
        }
        lm = result.pose_landmarks.landmark
        coords = {i: (lm[i].x * w, lm[i].y * h, lm[i].visibility) for i in range(len(lm))}
        angles = {b:angle_between(coords[a][:2], coords[b][:2], coords[c][:2]) for (a,b,c) in triplets.values()}
        mp_drawing.draw_landmarks(frame, result.pose_landmarks, connects, DrawingSpec(color=(0,255,0)), DrawingSpec(color=(0,255,0)),angles)
        #print(mp_pose.POSE_CONNECTIONS)

        other_points = {
            "L_Foot" : 29,
            "R_Foot" : 30,
            "Head" : 0,
        }

        for i, (name, (a, b, c)) in enumerate(triplets.items()):
            if coords[a][2] > 0.3 and coords[b][2] > 0.3 and coords[c][2] > 0.3:
                ang = angles[b]
                print(ang)
                put_angle(frame, ang, coords[b][:2], name)
                angles_row[i+1] = round(ang, 2)
        for i, (name, point) in enumerate(other_points.items()):
            if coords[point][2] > 0.3:
                color = (255,0,255)
                cv2.putText(frame, f"{name}",
                    (int(coords[point][0]), int(coords[point][1]) - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

    csv_writer.writerow(angles_row)
    outv.write(frame)

    cv2.imshow("boxing", cv2.resize(frame, (540,960)))
    if cv2.waitKey(1) & 0xFF == ord('q'): break

    frame_idx += 1
cap.release()
outv.release()
csv_file.close()
cv2.destroyAllWindows()
print(f"Saved {output_video} and {output_csv}")
