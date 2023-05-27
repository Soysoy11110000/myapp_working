import java.sql.Time;
import java.util.Scanner;

// 時給ベースで給与を算出して出力するプログラム
public class PayrollCalculationApp {
    
    // static 変数=>クラスの全インスタンスに共有される。（staticフィールド（クラス変数）） 
    // クラス名.変数名で呼び出す。（別クラスからも呼び出せる）
    // 計算用の数値を定数で用意
    final static long ONE_HOUR_BY_MILLI_SEC = 1000 * 60 * 60; // 1時間のミリ秒換算
    final static long ONE_MIN_BY_MILLI_SEC = 1000 * 60;       // 1分のミリ秒換算
    final static long ONE_HOUR_BY_MIN = 60;                   // 1時間の分換算
    // 計算用の労働時間を定義
    final static long sixHoursTime = 6 * ONE_HOUR_BY_MILLI_SEC;
    final static long eightHoursTime = 8 * ONE_HOUR_BY_MILLI_SEC;
    // 残業乗算率(超過分を１．２５倍の給与算出する)
    final static double overtimePayRate = 1.25;
    // 時給
    final static int hourlyWage = 900;
    // 分給
    final static int minutesWage = hourlyWage / 60;
    // 日給
    final static int dailyPay = 8 * hourlyWage;
    
    public static void main(String[] args) {

        // 出勤時刻・退勤時刻を受け取る
        // Timeオブジェクトに変換
        Scanner scan = new Scanner(System.in);
        System.out.println("出勤時刻");
        Time startTime = Time.valueOf(scan.next());
        System.out.println("出勤時刻");
        Time endTime =  Time.valueOf(scan.next());

        System.out.format("出勤時刻： %s%n", startTime);
        System.out.format("退勤時刻： %s%n", endTime);
        scan.close();

        // 出勤時刻-退勤時刻(long型)（ミリ秒単位)
        long workingTime = endTime.getTime() - startTime.getTime();
        // 労働時間が６時間超～８時間以下は、４５分の休憩
        long restTime = 0;
        if(workingTime <= sixHoursTime){
            //restTimeに変更なし
        }else if((workingTime > sixHoursTime) && (workingTime <= eightHoursTime)){
            restTime = 45 * ONE_MIN_BY_MILLI_SEC;
        // ８時間超は１時間の休憩
        }else if(workingTime > eightHoursTime){
            restTime = 1 * ONE_HOUR_BY_MILLI_SEC;
        }
        // 休憩時間を減算し、８時間を超過する場合
        // 実労働時間
        long actualWorkingTime = workingTime - restTime;
        // 給与算出
        int salary = PayrollCalculationApp.PayrollCalc(actualWorkingTime);

        // 時刻フォーマット変換

        // 時・分に分割
        // ミリ秒から〇時〇分に戻す
        int workingHour = (int)(workingTime / ONE_HOUR_BY_MILLI_SEC);
        int workingMin = (int)((workingTime / ONE_MIN_BY_MILLI_SEC) % ONE_HOUR_BY_MIN);
        // 結果を出力
        System.out.format("本日の労働時間： %s時間%s分%n", workingHour, workingMin);
        System.out.format("本日の給料： %d円%n", salary);

    }

    // 冒頭にstatic=>クラスメソッド
    // 実労働時間を受け取り、日給を換算する
    static int PayrollCalc(long actualWorkingTime){
        int salary = 0;

        if(actualWorkingTime > eightHoursTime){
            long overTime = actualWorkingTime - eightHoursTime;
            long overMinutes = overTime / ONE_MIN_BY_MILLI_SEC;
            // 残業手当
            int overTimePay = (int)(overMinutes * minutesWage * overtimePayRate);
            // 給与算出
            salary = dailyPay + overTimePay;
        }else{
            salary = dailyPay;
        }

        return salary;
    }
}