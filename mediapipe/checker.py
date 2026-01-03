def stance_right_elbow(angle):
    if angle < 45:
        return True
    return False

def stance_right_hand(angle):
    if angle < 40 and angle > 25:
        return True
    return False

def stance_left_elbow(angle):
    if angle < 30:
        return True
    return False

def stance_left_hand(angle):
    if angle < 40:
        return True
    return False

def stance_r_hip(angle):
    if angle < 100 and angle > 80:
        return True
    return False

def stance_left_knee(angle):
    if angle < 180 and angle > 160:
        return True
    return False

def stance_right_knee(angle):
    if angle < 150 and angle > 130:
        return True
    return False

def stance_l_shoulder(angle):
    if angle < 15:
        return True
    return False

def stance_r_shoulder(angle):
    if angle < 25:
        return True
    return False

def is_stance_correct(l_hand, r_hand, l_elbow, r_elbow, l_knee, r_knee, r_hip, l_shoulder, r_shoulder):
    errors = {"l_hand": "", "r_hand": "", "l_elbow": "", "r_elbow": "", "l_knee": "", "r_knee": "", "r_hip": "", "l_shoulder": "", "r_shoulder": ""}
    is_correct = True
    if not stance_left_hand(l_hand):
        is_correct = False
        errors["l_hand"] = "Keep your left hand up."
    if not stance_right_hand(r_hand):
        is_correct = False
        errors["r_hand"] = "Keep your right hand up."
    if not stance_left_elbow(l_elbow):
        is_correct = False
        errors["l_elbow"] = "Keep your left hand up."
    if not stance_right_elbow(r_elbow):
        is_correct = False
        errors["r_elbow"] = "Keep your right hand up."
    if not stance_left_knee(l_knee):
        is_correct = False
        errors["l_knee"] = "Keep your left knee slightly bent."
    if not stance_right_knee(r_knee):
        is_correct = False
        errors["r_knee"] = "Keep your right knee slightly bent."
    if not stance_r_hip(r_hip):
        is_correct = False
        errors["r_hip"] = "Keep your hip square with your shoulders."
    if not stance_l_shoulder(l_shoulder):
        is_correct = False
        errors["l_shoulder"] = "Tuck your left elbow in"
    if not stance_r_shoulder(r_shoulder):
        is_correct = False
        errors["r_shoulder"] = "Tuck your right elbow in"
    return is_correct, errors

def straight_right_elbow(angle):
    if angle > 160 and angle < 185:
        return True
    return False
def straight_right_hand(angle):
    if angle < 12:
        return True
    return False
def straight_left_elbow(angle):
    if angle > 0 and angle < 20:
        return True
    return False
def straight_r_hip(angle):
    if angle < 110 and angle > 90:
        return True
    return False
def straight_left_knee(angle):
    if angle < 180 and angle > 160:
        return True
    return False
def is_straight_correct(r_hand, l_elbow, r_elbow, l_knee, r_hip):
    errors = {"l_hand": "", "r_hand": "", "l_elbow": "", "r_elbow": "", "l_knee": "", "r_knee": "", "r_hip": ""}
    is_correct = True
    if not straight_right_hand(r_hand):
        is_correct = False
        errors["r_hand"] = "Keep your right hand straight."
    if not straight_left_elbow(l_elbow):
        is_correct = False
        errors["l_elbow"] = "Keep your left hand up."
    if not straight_right_elbow(r_elbow):
        is_correct = False
        errors["r_elbow"] = "Keep your right elbow straight."
    if not straight_left_knee(l_knee):
        is_correct = False
        errors["l_knee"] = "Keep your left knee slightly bent."
    if not straight_r_hip(r_hip):
        is_correct = False
        errors["r_hip"] = "Keep your r_hip aligned with your shoulders."
    return is_correct, errors
