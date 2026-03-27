import csv
import numpy as np
from pathlib import Path

np.set_printoptions(precision=2, suppress=True)

camera_angles = ["front", "back"]
move_list = ["guard", "jab", "straight", "leadHook", "rearHook"]
header = ["image", "L_Hand", "R_Hand", "L_Elbow", "R_Elbow", "L_Knee", "R_Knee",
            "L_Shoulder", "R_Shoulder", "L_Hip", "R_Hip"]

for camera_angle in camera_angles:
    print("Camera Angle:", camera_angle)
    csv_file = open(f"analysisCsvs/{camera_angle}.csv", "w", newline="")
    csv_writer = csv.writer(csv_file)
    csv_writer.writerow(header)
    for move in move_list:
        print("Move:", move)
        path = Path(f"outputCsvs/{camera_angle}/{move}.csv")
        csv_readings = np.genfromtxt(path, delimiter=",", skip_header=1)
        pure_readings = csv_readings[:, 1:]
        no_nan_readings = np.ma.masked_array(pure_readings, np.isnan(pure_readings))
        #print(pure_readings)
        csv_min = np.min(no_nan_readings, axis=0)
        csv_max = np.max(no_nan_readings, axis=0)
        print(["Minimum"] + list(csv_min))
        print(["Maximum"] + list(csv_max))
        csv_writer.writerow(["Minimum"] + list(csv_min))
        csv_writer.writerow(["Maximum"] + list(csv_max))

    csv_file.close()