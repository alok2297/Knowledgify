package com.gap.mobigpk1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gap.mobigpk1.FirebaseRepo;
import com.gap.mobigpk1.Model.ParentItem;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class FirebaseViewModel extends ViewModel implements FirebaseRepo.OnRealtimeDbTaskComplete {

    private MutableLiveData<List<ParentItem>> parentItemMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();
    private FirebaseRepo firebaseRepo;

    public MutableLiveData<List<ParentItem>> getParentItemMutableLiveData() {
        return parentItemMutableLiveData;
    }

    public MutableLiveData<DatabaseError> getDatabaseErrorMutableLiveData() {
        return databaseErrorMutableLiveData;
    }

    public FirebaseViewModel() {
        firebaseRepo = new FirebaseRepo(this);
    }

    public void getAllData() {
        firebaseRepo.getAllData();
    }

    @Override
    public void onSuccess(List<ParentItem> parentItemList) {
        parentItemMutableLiveData.setValue(parentItemList);
    }

    @Override
    public void onFailure(DatabaseError error) {
        databaseErrorMutableLiveData.setValue(error);
    }
}
