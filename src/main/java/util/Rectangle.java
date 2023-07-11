package util;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.w3c.dom.css.Rect;

@Getter
@AllArgsConstructor
public class Rectangle {
    /*

        (p1)----------(p2)
         |              |
         |              |
         |              |
        (p4)----------(p3)

     */
    private Point p1; //top left
    private Point p2; //top right
    private Point p3; //bottom left
    private Point p4; //bottom right

    public float height() {
        return Math.abs(p3.getY() - p2.getY());
    }

    public float width() {
        return Math.abs(p3.getX() - p4.getX());
    }

    public Pair<Rectangle,Rectangle> divide(Orientation orientation) {
        switch(orientation) {
            case VERTICAL:
                float xMid = (p2.getX() - p1.getX()) / 2f;
                Point topMid = new Point(xMid, p1.getY());
                Point bottomMid = new Point(xMid, p4.getY());

                Rectangle left = new Rectangle(p1, topMid, bottomMid, p4);
                Rectangle right = new Rectangle(topMid, p2, p3, bottomMid);

                return new Pair<>(left, right);
            case HORIZONTAL:
                float yMid = (p4.getY() - p1.getY()) / 2f;
                Point leftMid = new Point(p1.getX(), yMid);
                Point rightMid = new Point(p2.getX(), yMid);

                Rectangle top = new Rectangle(p1, p2, rightMid, leftMid);
                Rectangle bottom = new Rectangle(leftMid, rightMid, p3, p4);

                return new Pair<>(top, bottom);
        }
        return null;
    }

    public Point center() {
        return new Point(p2.getX() - p1.getX(), p3.getY() - p2.getY());
    }
}
