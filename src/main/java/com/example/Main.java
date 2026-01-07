package com.example;

import com.example.model.Matrix;
import com.example.service.MatrixMathematics;
import com.example.exception.NoSquareException;

public class Main {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Test de la librairie Matrix API");
        System.out.println("===========================================\n");

        try {
            System.out.println("Test 1: Création d'une matrice 2x2");
            Matrix matrix = new Matrix(2, 2);
            matrix.setValueAt(0, 0, 4.0);
            matrix.setValueAt(0, 1, 3.0);
            matrix.setValueAt(1, 0, 2.0);
            matrix.setValueAt(1, 1, 1.0);

            System.out.println("Matrice créée:");
            afficherMatrice(matrix);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'utilisation de la librairie:");
            e.printStackTrace();
        }
    }

    private static void afficherMatrice(Matrix matrix) {
        for (int i = 0; i < matrix.getNrows(); i++) {
            System.out.print("[ ");
            for (int j = 0; j < matrix.getNcols(); j++) {
                System.out.printf("%8.2f ", matrix.getValueAt(i, j));
            }
            System.out.println("]");
        }
    }
}