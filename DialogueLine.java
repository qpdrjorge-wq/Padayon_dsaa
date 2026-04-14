package entity;

import card.Buff;
import card.ItemGiftReward;

public class DialogueLine {
    public String text;
    public String[] choices;
    public int[] nextSequenceIndex;
    public int timeoutSeconds = 0;
    public int timeoutSequenceIndex = -1;
    public Buff[] choiceBuffs;

    public String requestedPropertyName = null;
    public int giveItemSequenceIndex = -1;
    public ItemGiftReward itemGiftReward = null;

    public DialogueLine(String text){
        this.text = text;
        this.choices = null;
        this.nextSequenceIndex = null;
        this.choiceBuffs = null;
    }

    public DialogueLine(String text, String[] choices, int[] nextSequenceIndex){
        this.text = text;
        this.choices = choices;
        this.nextSequenceIndex = nextSequenceIndex;
        this.choiceBuffs = null;
    }

    public DialogueLine(String text, String[] choices, int[] nextSequenceIndex,
                        Buff[] choiceBuffs) {
        this.text              = text;
        this.choices           = choices;
        this.nextSequenceIndex = nextSequenceIndex;
        this.choiceBuffs       = choiceBuffs;
    }

    // choice line with buff and timeout
    public DialogueLine(String text, String[] choices, int[] nextSequenceIndex,
                        Buff[] choiceBuffs,
                        int timeoutSeconds, int timeoutSequenceIndex) {
        this.text                 = text;
        this.choices              = choices;
        this.nextSequenceIndex    = nextSequenceIndex;
        this.choiceBuffs          = choiceBuffs;
        this.timeoutSeconds       = timeoutSeconds;
        this.timeoutSequenceIndex = timeoutSequenceIndex;
    }

    public DialogueLine(String text, String[] choices, int[] nextSequenceIndex,
                        int timeoutSeconds, int timeoutSequenceIndex) {
        this.text                 = text;
        this.choices              = choices;
        this.nextSequenceIndex    = nextSequenceIndex;
        this.choiceBuffs          = null;
        this.timeoutSeconds       = timeoutSeconds;
        this.timeoutSequenceIndex = timeoutSequenceIndex;
    }

    public DialogueLine withItemRequest(String propertyName, int giveSequenceIndex, ItemGiftReward reward){
        this.requestedPropertyName = propertyName;
        this.giveItemSequenceIndex = giveSequenceIndex;
        this.itemGiftReward = reward;
        return this;
    }

    public DialogueLine withTimeout(int seconds, int sequenceIndex) {
        this.timeoutSeconds = seconds;
        this.timeoutSequenceIndex = sequenceIndex;
        return this;
    }


    public boolean hasChoices(){
        return choices != null && choices.length > 0;
    }

    public Buff getBuffForChoice(int choiceIndex) {
        if (choiceBuffs == null) return null;
        if (choiceIndex < 0 || choiceIndex >= choiceBuffs.length) return null;
        return choiceBuffs[choiceIndex];
    }

    public boolean hasItemRequest() {
        return requestedPropertyName != null;
    }

    // Add these fields
    public card.CurseCard.CurseEffect[] curseEffects;
    public int[] curseValues;

    // Add this builder method so you can easily chain it like .withItemRequest()
    public DialogueLine withCurseReward(card.CurseCard.CurseEffect[] effects, int[] values) {
        this.curseEffects = effects;
        this.curseValues = values;
        return this;
    }

    // Add these exact getter methods
    public card.CurseCard.CurseEffect getCurseEffectForChoice(int index) {
        if (curseEffects != null && index >= 0 && index < curseEffects.length) {
            return curseEffects[index];
        }
        return null;
    }

    public int getCurseValueForChoice(int index) {
        if (curseValues != null && index >= 0 && index < curseValues.length) {
            return curseValues[index];
        }
        return 0;
    }

}
