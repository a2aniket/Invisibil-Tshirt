import cv2
import numpy as np
import time
cap = cv2.VideoCapture(0)
codec = cv2.VideoWriter_fourcc(*'XVID')
out = cv2.VideoWriter('Output.avi', codec, 20.0, (640, 480))
_, bag = cap.read()

while True:
    _, frame = cap.read()
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    lower = np.array([155, 160, 0])
    upper = np.array([255, 255, 255])
    mask = cv2.inRange(frame, lower, upper)
    mask_inv = cv2.bitwise_not(mask)
    fg = cv2.bitwise_and(frame, frame, mask = mask_inv)
    bg = cv2.bitwise_and(bag, bag, mask = mask)
    final = cv2.add(bg, fg)
    cv2.imshow("final", final)
    out.write(final)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
cap.release()
out.release()
cv2.destroyAllWindows()
