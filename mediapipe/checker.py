def stance_right_arm(angle):
    if angle < 45:
        return True
    return False

def stance_right_hand(angle):
    if angle < 45 and angle > 25:
        return True
    return False

def stance_left_arm(angle):
    if angle < 30:
        return True
    return False

def stance_left_hand(angle):
    if angle < 40:
        return True
    return False

def stance_hips(angle):
    if angle < 100 and angle > 80:
        return True
    return False

def stance_left_leg(angle):
    if angle < 180 and angle > 160:
        return True
    return False

def stance_right_leg(angle):
    if angle < 150 and angle > 130:
        return True
    return False

def is_stance_correct(l_hand, r_hand, l_arm, r_arm, l_leg, r_leg, hips):
    errors = {"l_hand": "", "r_hand": "", "l_arm": "", "r_arm": "", "l_leg": "", "r_leg": "", "hips": ""}
    is_correct = True
    if not stance_left_hand(l_hand):
        is_correct = False
        errors["l_hand"] = "Keep your left hand near your chin."
    if not stance_right_hand(r_hand):
        is_correct = False
        errors["r_hand"] = "Keep your right hand near your chin."
    if not stance_left_arm(l_arm):
        is_correct = False
        errors["l_arm"] = "Keep your left hand up."
    if not stance_right_arm(r_arm):
        is_correct = False
        errors["r_arm"] = "Keep your right hand up."
    if not stance_left_leg(l_leg):
        is_correct = False
        errors["l_leg"] = "Keep your left leg slightly bent."
    if not stance_right_leg(r_leg):
        is_correct = False
        errors["r_leg"] = "Keep your right leg slightly bent."
    if not stance_hips(hips):
        is_correct = False
        errors["hips"] = "Keep your hips square with your shoulders."
    return is_correct, errors

def straight_right_arm(angle):
    if angle > 160 and angle < 185:
        return True
    return False
def straight_right_hand(angle):
    if angle < 12:
        return True
    return False
def straight_left_arm(angle):
    if angle > 0 and angle < 20:
        return True
    return False
def straight_hips(angle):
    if angle < 110 and angle > 90:
        return True
    return False
def straight_left_leg(angle):
    if angle < 180 and angle > 160:
        return True
    return False
def is_straight_correct(r_hand, l_arm, r_arm, l_leg, hips):
    errors = {"l_hand": "", "r_hand": "", "l_arm": "", "r_arm": "", "l_leg": "", "r_leg": "", "hips": ""}
    is_correct = True
    if not straight_right_hand(r_hand):
        is_correct = False
        errors["r_hand"] = "Keep your right hand straight."
    if not straight_left_arm(l_arm):
        is_correct = False
        errors["l_arm"] = "Keep your left hand up."
    if not straight_right_arm(r_arm):
        is_correct = False
        errors["r_arm"] = "Keep your right arm straight."
    if not straight_left_leg(l_leg):
        is_correct = False
        errors["l_leg"] = "Keep your left leg slightly bent."
    if not straight_hips(hips):
        is_correct = False
        errors["hips"] = "Keep your hips aligned with your shoulders."
    return is_correct, errors
