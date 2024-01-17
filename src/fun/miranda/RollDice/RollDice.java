package fun.miranda.RollDice;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RollDice {
    public Integer sum = 0;
    public String result;
    private Integer r = 1;
    private StringBuilder numbersString;
    public boolean validate = true;

    public RollDice(Player player, String chat) {
        String playerName = player.getName();
        String pattern;
        int d = 2;
        int test = 0;
        boolean hasVerification;
        if (chat.contains(" ")) {
            pattern = "^\\.[rR](\\d+)[dD](\\d+) (\\d+)$";
            Matcher matcher = Pattern.compile(pattern).matcher(chat);
            if (matcher.find()) {
                this.r = Integer.parseInt(matcher.group(1));
                d = Integer.parseInt(matcher.group(2));
                test = Integer.parseInt(matcher.group(3));
            } else {
                validate = false;
            }
            hasVerification = true;
        } else {
            pattern = "^\\.[rR](\\d+)[dD](\\d+)$";
            Matcher matcher = Pattern.compile(pattern).matcher(chat);
            if (matcher.find()) {
                this.r = Integer.parseInt(matcher.group(1));
                d = Integer.parseInt(matcher.group(2));
            } else {
                validate = false;
            }
            hasVerification = false;
        }
        if (this.r <= 0 || d <= 1) {
            validate = false;
        }
        if (validate) {
            String playerCMD = String.format("r%sd%s", this.r, d);
            ArrayList<Integer> numbers = this.roll(this.r, d);
            for (int i : numbers) {
                this.sum += i;
            }
            if (hasVerification) {
                String verificationResult;
                if (this.r == 1 && d == 100) {

                    if (this.sum >= 95) {
                        verificationResult = "大失败";
                    } else if (this.sum <= 5) {
                        verificationResult = "大成功";
                    } else if (this.sum > test) {
                        verificationResult = "失败";
                    } else {
                        int hardToSuccess = test / 2;
                        int veryHardToSuccess = test / 5;
                        if (this.sum <= veryHardToSuccess) {
                            verificationResult = "极难成功";
                        } else if (this.sum <= hardToSuccess) {
                            verificationResult = "困难成功";
                        } else {
                            verificationResult = "成功";
                        }
                    }
                } else {
                    if (this.sum >= test) {
                        verificationResult = "失败";
                    } else {
                        verificationResult = "成功";
                    }
                }
                if (this.r != 1) {
                    this.result = String.format("§e玩家 §b%s §e检定 §d%s => %d §e结果: §a%s=%d §6%s", playerName, playerCMD, test, numbersString.toString(), sum, verificationResult);
                } else {
                    this.result = String.format("§e玩家 §b%s §e检定 §d%s => %d §e结果: §a%d §6%s", playerName, playerCMD, test, sum, verificationResult);
                }
            } else {
                if (this.r != 1) {
                    this.result = String.format("§e玩家 §b%s §d%s §e结果: §a%s=%d", playerName, playerCMD, numbersString.toString(), this.sum);
                } else {
                    this.result = String.format("§e玩家 §b%s §d%s §e结果: §a%d", playerName, playerCMD, this.sum);
                }
            }
        }
    }

    private ArrayList<Integer> roll(Integer r, Integer d) {
        ArrayList<Integer> out = new ArrayList<>();
        this.numbersString = new StringBuilder();
        for (int i = 0; i < r; i++) {
            int current = this.getRandomInteger(d);
            out.add(current);
            if (i != this.r - 1) {
                numbersString.append(current).append("+");
            } else {
                numbersString.append(current);
            }
        }
        return out;
    }


    private Integer getRandomInteger(Integer range) {
        Random random = new Random();
        return random.nextInt(range) + 1;
    }

}
