package core;

class EllepticCurve {
    private int a, b, p;

    EllepticCurve(int a, int b, int p){
        this.a = a;
        this.b = b;
        this.p = p;
    }

    // Умножаем по быстрому алгоритму удвоения-сложения
    // 41 == 0b101001 => 41P = 2^5P + 2^3P + 2^0P
    Point mul(Point p, int n){
        int tmp = 1, power = -1;
        Point resP = null, tmpP = new Point(p.getX(), p.getY());;

        while (tmp <= n) {
            power++;
            tmp = tmp << 1;
        }

        tmp = n;
        boolean first = true;
        for (int i = 0; i <= power; i++){
            if((tmp & 1) == 1) {
                if(first) {
                    resP = new Point(tmpP.getX(), tmpP.getY());
                    first = false;
                }
                else
                    resP = this.sum(resP, tmpP);

            }

            tmpP = this.sum(tmpP, tmpP);
            tmp = tmp >>> 1;
        }

        return resP;
    }

    // Метод расчета суммы двух точек на элептической кривой
    Point sum(Point p1, Point p2) {

        /*
            x3 = λ^2 - x1 - x2(mod p)
            y3 = λ(x1 - x3) - y1(mod p)
        */
        int lambda = calcLambda(p1, p2);
        int tmp = lambda * lambda - p1.getX() - p2.getX();
        int newX = tmp >= 0 ? tmp % this.p : this.p + (tmp % this.p);

        tmp = lambda * (p1.getX() -newX) - p1.getY();
        int newY = tmp >= 0 ? tmp % this.p : this.p + (tmp % this.p) ;

        Point res = new Point(newX, newY);


        return new Point(newX, newY);
    }

    // метод расчета наклона прямой, проходящей через две точки
    private int calcLambda(Point p1, Point p2 ) {
        int numerator, denominator;

        // p1 == p2: λ = (3x1^2 + a) / 2y1
        // p1 != p2: λ = (y2 - y1) / (x2 - x1)
        if (p1.equals(p2)) {
            numerator = 3 * p1.getX() * p1.getX() + this.a;
            denominator = 2 * p1.getY();
        }
        else {
            numerator = p2.getY() - p1.getY();
            denominator = p2.getX() - p1.getX();
        }

        // a / b = a * b^(-1); Ищем обратную величину по модулю
        denominator = this.invMod(denominator);

        // если вычисленное значение получится отрицательным, приводим к положительному
        return (numerator * denominator < 0) ?
            this.p + (numerator * denominator) % this.p :
            (numerator * denominator) % this.p;
        }


    //Возвращает обратную величину n по модулю p
    //такое целое число m, при котором (n*m)%p == 1
    //Применяется расширенный алгоритм Евклида
    private int invMod(int n){
        int s = 0, oldS = 1;
        int r = this.p, oldR = n;

        while (r != 0){
            int quotient = (int)Math.floorDiv(oldR , r);
            int tmp = r;
            r = oldR - (quotient * r);
            oldR = tmp;

            tmp = s;
            s = oldS - quotient * s;
            oldS = tmp;

        }

        //учитываем отрицательный результат
        return oldS + (oldS < 0 ? this.p : 0);
    }
}
