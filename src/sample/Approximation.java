package sample;

import java.util.ArrayList;

class Approximation {
    private ArrayList<LineModel> models;
    private int n;
    private int k;

    Approximation(ArrayList<LineModel> models, int n, int k) {
        this.models = models;
        this.n = n;
        this.k = k;
    }

    /**
     * Функция для расчёта коэффициентов параболы по методу наименьших квадратов
     *
     * @param models коллекция точек x и y где x - это speed, а y - это headway
     * @param n      количество точек
     * @param k      степень полинома
     * @return массив с полученными коэффицентами
     */
    private double[] mnk(ArrayList<LineModel> models, int n, int k) {
        double result[] = new double[k + 1];  //результат
        double matrix[][] = new double[k + 1][k + 1];
        double free[] = new double[k + 1];

        // заполняем матрицу для поиска коэффициентов
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= k; j++) {
                matrix[i][j] = 0;
                for (int l = 0; l < n; l++)
                    matrix[i][j] += Math.pow(models.get(l).getSpead(), i + j);
            }
        }
        // заполняем матрицу свободых членов
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j < n; j++)
                free[i] += Math.pow(models.get(j).getSpead(), i) * models.get(j).getHeadvay();
        }

        double temp;
        for (int i = 0; i <= k; i++) {
            if (matrix[i][i] == 0) {
                for (int j = 0; j <= k; j++) {
                    if (j == i) continue;
                    if (matrix[j][i] != 0 && matrix[i][j] != 0) {
                        for (int l = 0; l <= k; l++) {
                            temp = matrix[j][l];
                            matrix[j][l] = matrix[i][l];
                            matrix[i][l] = temp;
                        }
                        temp = free[j];
                        free[j] = free[i];
                        free[i] = temp;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i <= k; i++) {
            for (int j = i + 1; j <= k; j++) {
                double M = matrix[j][i] / matrix[i][i];
                for (int m = i; m <= k; m++) {
                    matrix[j][m] -= M * matrix[i][m];
                }
                free[j] -= M * free[i];
            }
        }


        for (int i = k; i >= 0; i--) {
            double sumkoef = 0;
            for (int j = i; j <= k; j++) {
                sumkoef += matrix[i][j] * result[j];
            }
            result[i] = (free[i] - sumkoef) / matrix[i][i];
        }


        return result;
    }

    /**
     * Функция для расчёта приближенных значений ординат используя метод наименьших квадратов
     *
     * @return массив с приближенными значениями ординат
     */
    double[] getValues() {
        double[] result = mnk(models, n, k);
        double[] values = new double[models.size()];
        int j = 0;
        for (LineModel model : models) {
            values[j] = 0;
            for (int i = result.length - 1; i >= 0; i--) {
                values[j] += Math.pow(model.getSpead(), i) * result[i];
            }
            j++;
        }
        return values;
    }
}
