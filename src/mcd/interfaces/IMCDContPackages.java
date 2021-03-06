package mcd.interfaces;

import mcd.services.IMCDModelService;

// Implémentée par les classes qui peuvent contenir des paquetages
public interface IMCDContPackages {

    public abstract String getNamePath(int pathMode);

    public static IMCDContPackages getIMCDContPackagesByNamePath(IMCDModel imcdModel, int pathMode, String namePath){
        for (IMCDContPackages imcdContPackages: IMCDModelService.getIMCDContPackages(imcdModel)){
            if (imcdContPackages.getNamePath(pathMode).equals(namePath)){
                return imcdContPackages;
            }
        }
        return null;
    }

}
