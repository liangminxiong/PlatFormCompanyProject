package com.yuefeng.utils;

import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.common.utils.Constans;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.features.modle.carlist.CarListInfosMsgBean;
import com.yuefeng.features.modle.carlist.Organ;
import com.yuefeng.features.modle.carlist.OrgansBean;
import com.yuefeng.features.modle.carlist.VehiclesBean;
import com.yuefeng.features.modle.carlist.VehiclesBeanX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class DatasUtils {

    private static final String TAG = "tag";
    private static int firstOsize;
    private static String secondId;
    private static String secondOrgShortName;
    private static String firstVRegistrationNO;
    private static int firstVsize;
    private static List<VehiclesBean> secondVehicles;
    private static int secondVsize;
    private static String firstVehicleId;
    private static String secondVehicleId;
    private static String secondVehicleRegistrationNO;
    private static int secondOsize;
    private static String thirdOrgansId;
    private static String thirdOrgansOrgShortName;
    private static List<VehiclesBean> thirdVehicles;
    private static int thirdVsize;
    private static List<Organ> thirdOrgans;
    private static int thirdOsize;
    private static String thirdVehicleId;
    private static String thirdVehicleRegistrationNO;
    private static String thirdOrganId;
    private static String thirdOrganOrgShortName;
    private static List<VehiclesBean> fourthvehicles;
    private static int fourthVsize;
    private static List<Organ> fourthOrgans;
    private static int fourthOsize;
    private static String fourthvehicleId;
    private static String fourthvehicleRegistrationNO;
    private static String fourthOrganId;
    private static String fourthOrganOrgShortName;
    private static List<VehiclesBean> fifthVehicles;
    private static int fifthVsize;
    private static List<Organ> fifthOrgans;
    private static String fifthVehicleId;
    private static String fifthVehicleRegistrationNO;
    private static int fifthOsize;
    private static String fifthOrganId;
    private static String fifthOrganOrgShortName;
    private static List<VehiclesBean> sixthVehicles;
    private static int sixthVsize;
    private static List<Organ> sixthOrganOrgans;
    private static int sixthOsize;
    private static String firstVehicleStateType;
    private static String secondVehicleStateType;
    private static String thirdVehicleStateType;
    private static String fourthvehicleStateType;
    private static String fifthVehicleStateType;
    private static String sixthVehicleId;
    private static String sixthVehicleRegistrationNO;
    private static String sixthVehicleStateType;
    private static String firstVehicleTerminalNO;
    private static String secondVehicleTerminalNO;
    private static String thirdVehicleTerminalNO;
    private static String fourthvehicleTerminalNO;
    private static String fifthVehicleTerminalNO;
    private static String sixthVehicleTerminalNO;

    private static String stateType2 = "";
    private static String sStateType3 = "";
    private static String address ="";
    private static LatLng sLatLng;

    public static List<Node> ReturnTreesDatas(List<CarListInfosMsgBean> msg) {

        List<Node> mDatas = new ArrayList<Node>();

        int size = msg.size();
        if (size <= 0) {
            return new ArrayList<Node>();
        }

        String treeStr = initDatasTreeStr(msg);

        CarListInfosMsgBean msgBean = msg.get(0);

        String orgShortName = msgBean.getOrgShortName();
        String fatherPid = msgBean.getPid();
        String fatherId = msgBean.getId();


        List<VehiclesBeanX> firstVehicles = msgBean.getVehicles();
        firstVsize = firstVehicles.size();

        List<OrgansBean> firstOrgans = msgBean.getOrgans();
        firstOsize = firstOrgans.size();
        /*第一层*/
        // id , pid , label , 其他属性
        mDatas.add(new Node(fatherId, fatherPid, orgShortName, treeStr, "", "", "", "", "", ""));

        if (firstVsize > 0) {
            for (VehiclesBeanX firstVehicle : firstVehicles) {
                firstVehicleId = firstVehicle.getId();
                firstVRegistrationNO = firstVehicle.getRegistrationNO();
                firstVehicleStateType = firstVehicle.getStateType();
                firstVehicleTerminalNO = firstVehicle.getTerminalNO();
                String gt = firstVehicle.getGt();
                String speed = firstVehicle.getSpeed();
                String obd = firstVehicle.getObd();
                mDatas.add(new Node(firstVehicleId + "", fatherId + "", firstVRegistrationNO,
                        Constans.COUNT_ZERO, firstVehicleStateType, firstVehicleTerminalNO, gt, speed, obd, address));
            }
        }


         /*第二层*/
        if (firstOsize > 0) {
            for (OrgansBean firstOrgan : firstOrgans) {
                String strSecond = initDatasTreeStrSecond(firstOrgan);

                secondId = firstOrgan.getId();
                secondOrgShortName = firstOrgan.getOrgShortName();


                secondVehicles = firstOrgan.getVehicles();
                secondVsize = secondVehicles.size();

                List<Organ> organs = firstOrgan.getOrgans();
                secondOsize = organs.size();


                mDatas.add(new Node(secondId + "", fatherId + "", secondOrgShortName, strSecond, "", "", "", "", "", ""));


                if (secondVsize > 0) {
                    for (VehiclesBean secondVehicle : secondVehicles) {
                        secondVehicleId = secondVehicle.getId();
                        secondVehicleRegistrationNO = secondVehicle.getRegistrationNO();
                        secondVehicleStateType = secondVehicle.getStateType();
                        secondVehicleTerminalNO = secondVehicle.getTerminalNO();
                        String gt = secondVehicle.getGt();
                        String speed = secondVehicle.getSpeed();
                        String obd = secondVehicle.getObd();
                        mDatas.add(new Node(secondVehicleId + "", secondId + "", secondVehicleRegistrationNO,
                                Constans.COUNT_ZERO, secondVehicleStateType, secondVehicleTerminalNO, gt, speed, obd, address));
                    }
                }

//                第三层
                if (secondOsize > 0) {

                    for (Organ organ : organs) {
                        String strThird = initDatasTreeStrThird(organ);
                        thirdOrgansId = organ.getId();
                        thirdOrgansOrgShortName = organ.getOrgShortName();

                        thirdVehicles = organ.getVehicles();
                        thirdVsize = thirdVehicles.size();

                        thirdOrgans = organ.getOrgans();
                        thirdOsize = thirdOrgans.size();

//                        第三层总数据
                        mDatas.add(new Node(thirdOrgansId + "", secondId + "", thirdOrgansOrgShortName, strThird, "", "", "", "", "", ""));


                        if (thirdVsize > 0) {
                            for (VehiclesBean thirdVehicle : thirdVehicles) {
                                thirdVehicleId = thirdVehicle.getId();
                                thirdVehicleRegistrationNO = thirdVehicle.getRegistrationNO();
                                thirdVehicleStateType = thirdVehicle.getStateType();
                                thirdVehicleTerminalNO = thirdVehicle.getTerminalNO();
                                String gt = thirdVehicle.getGt();
                                String speed = thirdVehicle.getSpeed();
                                String obd = thirdVehicle.getObd();
                                mDatas.add(new Node(thirdVehicleId + "", thirdOrgansId + "", thirdVehicleRegistrationNO,
                                        Constans.COUNT_ZERO, thirdVehicleStateType, thirdVehicleTerminalNO, gt, speed, obd, address));
                            }
                        }

//                        第四层
                        if (thirdOsize > 0) {
                            for (Organ thirdOrgan : thirdOrgans) {

                                String strFouth = initDatasTreeStrFouth(thirdOrgan);

                                thirdOrganId = thirdOrgan.getId();
                                thirdOrganOrgShortName = thirdOrgan.getOrgShortName();

                                fourthvehicles = thirdOrgan.getVehicles();
                                fourthVsize = fourthvehicles.size();

                                fourthOrgans = thirdOrgan.getOrgans();
                                fourthOsize = fourthOrgans.size();

                                mDatas.add(new Node(thirdOrganId + "", thirdOrgansId + "", thirdOrganOrgShortName, strFouth, "", "", "", "", "", ""));


                                if (fourthVsize > 0) {
                                    for (VehiclesBean fourthvehicle : fourthvehicles) {
                                        fourthvehicleId = fourthvehicle.getId();
                                        fourthvehicleRegistrationNO = fourthvehicle.getRegistrationNO();
                                        fourthvehicleStateType = fourthvehicle.getStateType();
                                        fourthvehicleTerminalNO = fourthvehicle.getTerminalNO();
                                        String gt = fourthvehicle.getGt();
                                        String speed = fourthvehicle.getSpeed();
                                        String obd = fourthvehicle.getObd();
                                        mDatas.add(new Node(fourthvehicleId + "", thirdOrganId + "", fourthvehicleRegistrationNO,
                                                Constans.COUNT_ZERO, fourthvehicleStateType, fourthvehicleTerminalNO, gt, speed, obd, address));
                                    }
                                }

//                                第五层
                                if (fourthOsize > 0) {
                                    for (Organ fourthOrgan : fourthOrgans) {

                                        String strFifth = initDatasTreeStrFouth(fourthOrgan);
                                        fourthOrganId = fourthOrgan.getId();
                                        fourthOrganOrgShortName = fourthOrgan.getOrgShortName();

                                        fifthVehicles = fourthOrgan.getVehicles();
                                        fifthVsize = fifthVehicles.size();

                                        fifthOrgans = fourthOrgan.getOrgans();
                                        fifthOsize = fifthOrgans.size();


                                        mDatas.add(new Node(fourthOrganId + "", thirdOrganId + "", fourthOrganOrgShortName, strFifth, "", "", "", "", "", ""));


                                        if (fifthVsize > 0) {
                                            for (VehiclesBean fifthVehicle : fifthVehicles) {
                                                fifthVehicleId = fifthVehicle.getId();
                                                fifthVehicleRegistrationNO = fifthVehicle.getRegistrationNO();
                                                fifthVehicleStateType = fifthVehicle.getStateType();
                                                fifthVehicleTerminalNO = fifthVehicle.getTerminalNO();
                                                String gt = fifthVehicle.getGt();
                                                String speed = fifthVehicle.getSpeed();
                                                String obd = fifthVehicle.getObd();
                                                mDatas.add(new Node(fifthVehicleId + "", thirdOrganId + "", fifthVehicleRegistrationNO,
                                                        Constans.COUNT_ZERO, fifthVehicleStateType, fifthVehicleTerminalNO, gt, speed, obd, address));
                                            }

                                        }

//                                        第六层
                                        if (fifthOsize > 0) {
                                            for (Organ fifthOrgan : fifthOrgans) {

                                                String strSixth = initDatasTreeStrFouth(fourthOrgan);
                                                fifthOrganId = fifthOrgan.getId();
                                                fifthOrganOrgShortName = fifthOrgan.getOrgShortName();

                                                sixthVehicles = fifthOrgan.getVehicles();
                                                sixthVsize = sixthVehicles.size();

                                                sixthOrganOrgans = fifthOrgan.getOrgans();
                                                sixthOsize = sixthOrganOrgans.size();

                                                mDatas.add(new Node(fifthOrganId + "", fourthOrganId + "", fifthOrganOrgShortName, strSixth, "", "", "", "", "", ""));


                                                if (sixthVsize > 0) {
                                                    for (VehiclesBean sixthVehicle : sixthVehicles) {
                                                        sixthVehicleId = sixthVehicle.getId();
                                                        sixthVehicleRegistrationNO = sixthVehicle.getRegistrationNO();
                                                        sixthVehicleStateType = sixthVehicle.getStateType();
                                                        sixthVehicleTerminalNO = sixthVehicle.getTerminalNO();
                                                        String gt = sixthVehicle.getGt();
                                                        String speed = sixthVehicle.getSpeed();
                                                        String obd = sixthVehicle.getObd();
                                                        mDatas.add(new Node(sixthVehicleId + "", fifthOrganId + "",
                                                                sixthVehicleRegistrationNO, Constans.COUNT_ZERO, sixthVehicleStateType,
                                                                sixthVehicleTerminalNO, gt, speed, obd, address));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return mDatas;
    }


    private static String initDatasTreeStr(List<CarListInfosMsgBean> msg) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;

        int size = msg.size();
        if (size <= 0) {
            return "";
        }
        CarListInfosMsgBean msgBean = msg.get(0);

        /*第二层*/
        List<VehiclesBeanX> firstVehicles = msgBean.getVehicles();
        firstVsize = firstVehicles.size();

        List<OrgansBean> firstOrgans = msgBean.getOrgans();
        firstOsize = firstOrgans.size();

        // id , pid , label , 其他属性

        if (firstVsize > 0) {
            for (VehiclesBeanX firstVehicle : firstVehicles) {
                firstVehicleStateType = firstVehicle.getStateType();
                countAll++;
                if (firstVehicleStateType.contains("1")) {
                    count++;
                } else if (firstVehicleStateType.contains("2")) {
                    count++;
                }
            }
        }

        if (firstOsize > 0) {
            for (OrgansBean organ : firstOrgans) {

                secondVehicles = organ.getVehicles();
                secondVsize = secondVehicles.size();

                List<Organ> organs = organ.getOrgans();
                secondOsize = organs.size();
                if (secondVsize > 0) {
                    for (VehiclesBean secondVehicle : secondVehicles) {
                        stateType2 = secondVehicle.getStateType();
                        countAll++;
                        if (stateType2.contains("1")) {
                            count++;
                        } else if (stateType2.contains("2")) {
                            count++;
                        }
                    }
                }


//                第三层
                if (secondOsize > 0) {
                    for (Organ secondOrgans : organs) {

                        thirdVehicles = secondOrgans.getVehicles();
                        thirdVsize = thirdVehicles.size();

                        thirdOrgans = secondOrgans.getOrgans();
                        thirdOsize = thirdOrgans.size();

                        if (thirdVsize > 0) {
                            for (VehiclesBean thirdVehicle : thirdVehicles) {
                                countAll++;
                                sStateType3 = thirdVehicle.getStateType();
                                if (sStateType3.contains("1")) {
                                    count++;
                                } else if (sStateType3.contains("2")) {
                                    count++;
                                }
                            }
                        }


//                        第四层
                        if (thirdOsize > 0) {
                            for (Organ thirdOrgan : thirdOrgans) {

                                fourthvehicles = thirdOrgan.getVehicles();
                                fourthVsize = fourthvehicles.size();

                                fourthOrgans = thirdOrgan.getOrgans();
                                fourthOsize = fourthOrgans.size();

                                String stateType4 = "";
                                if (fourthVsize > 0) {
                                    for (VehiclesBean fourthvehicle : fourthvehicles) {
                                        countAll++;
                                        stateType4 = fourthvehicle.getStateType();
                                        if (stateType4.contains("1")) {
                                            count++;
                                        } else if (stateType4.contains("2")) {
                                            count++;
                                        }
                                    }
                                }

//                                第五层
                                if (fourthOsize > 0) {
                                    for (Organ fourthOrgan : fourthOrgans) {

                                        fifthVehicles = fourthOrgan.getVehicles();
                                        fifthVsize = fifthVehicles.size();

                                        fifthOrgans = fourthOrgan.getOrgans();
                                        fifthOsize = fifthOrgans.size();


                                        String stateType5 = "";
                                        if (fifthVsize > 0) {
                                            for (VehiclesBean fifthVehicle : fifthVehicles) {
                                                countAll++;
                                                stateType5 = fifthVehicle.getStateType();
                                                if (stateType5.equals("1")) {
                                                    count++;
                                                } else if (stateType5.equals("2")) {
                                                    count++;
                                                }
                                            }
                                        }

//                                        第六层
                                        if (fifthOsize > 0) {
                                            for (Organ fifthOrgan : fifthOrgans) {
                                                sixthVehicles = fifthOrgan.getVehicles();
                                                sixthVsize = sixthVehicles.size();

                                                sixthOrganOrgans = fifthOrgan.getOrgans();
                                                sixthOsize = sixthOrganOrgans.size();

                                                String stateType6 = "";
                                                if (sixthVsize > 0) {
                                                    for (VehiclesBean sixthVehicle : sixthVehicles) {
                                                        stateType6 = sixthVehicle.getStateType();
                                                        countAll++;
                                                        if (stateType6.contains("1")) {
                                                            count++;
                                                        } else if (stateType6.contains("2")) {
                                                            count++;
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        countAllStr = count + "/" + countAll;
        return countAllStr;
    }

    private static String initDatasTreeStrSecond(OrgansBean firstOrgans) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;


        secondVehicles = firstOrgans.getVehicles();
        secondVsize = secondVehicles.size();

        List<Organ> organs = firstOrgans.getOrgans();
        secondOsize = organs.size();
        if (secondVsize > 0) {
            for (VehiclesBean secondVehicle : secondVehicles) {
                stateType2 = secondVehicle.getStateType();
                countAll++;
                if (stateType2.contains("1")) {
                    count++;
                } else if (stateType2.contains("2")) {
                    count++;
                }
            }
        }


//                第三层
        if (secondOsize > 0) {
            for (Organ secondOrgans : organs) {

                thirdVehicles = secondOrgans.getVehicles();
                thirdVsize = thirdVehicles.size();

                thirdOrgans = secondOrgans.getOrgans();
                thirdOsize = thirdOrgans.size();

                if (thirdVsize > 0) {
                    for (VehiclesBean thirdVehicle : thirdVehicles) {
                        countAll++;
                        sStateType3 = thirdVehicle.getStateType();
                        if (sStateType3.contains("1")) {
                            count++;
                        } else if (sStateType3.contains("2")) {
                            count++;
                        }
                    }
                }


//                        第四层
                if (thirdOsize > 0) {
                    for (Organ thirdOrgan : thirdOrgans) {

                        fourthvehicles = thirdOrgan.getVehicles();
                        fourthVsize = fourthvehicles.size();

                        fourthOrgans = thirdOrgan.getOrgans();
                        fourthOsize = fourthOrgans.size();

                        String stateType4 = "";
                        if (fourthVsize > 0) {
                            for (VehiclesBean fourthvehicle : fourthvehicles) {
                                countAll++;
                                stateType4 = fourthvehicle.getStateType();
                                if (stateType4.contains("1")) {
                                    count++;
                                } else if (stateType4.contains("2")) {
                                    count++;
                                }
                            }
                        }

//                                第五层
                        if (fourthOsize > 0) {
                            for (Organ fourthOrgan : fourthOrgans) {

                                fifthVehicles = fourthOrgan.getVehicles();
                                fifthVsize = fifthVehicles.size();

                                fifthOrgans = fourthOrgan.getOrgans();
                                fifthOsize = fifthOrgans.size();


                                String stateType5 = "";
                                if (fifthVsize > 0) {
                                    for (VehiclesBean fifthVehicle : fifthVehicles) {
                                        countAll++;
                                        stateType5 = fifthVehicle.getStateType();
                                        if (stateType5.equals("1")) {
                                            count++;
                                        } else if (stateType5.equals("2")) {
                                            count++;
                                        }
                                    }
                                }

//                                        第六层
                                if (fifthOsize > 0) {
                                    for (Organ fifthOrgan : fifthOrgans) {
                                        sixthVehicles = fifthOrgan.getVehicles();
                                        sixthVsize = sixthVehicles.size();

                                        sixthOrganOrgans = fifthOrgan.getOrgans();
                                        sixthOsize = sixthOrganOrgans.size();

                                        String stateType6 = "";
                                        if (sixthVsize > 0) {
                                            for (VehiclesBean sixthVehicle : sixthVehicles) {
                                                stateType6 = sixthVehicle.getStateType();
                                                countAll++;
                                                if (stateType6.contains("1")) {
                                                    count++;
                                                } else if (stateType6.contains("2")) {
                                                    count++;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        countAllStr = count + "/" + countAll;
        return countAllStr;
    }


    private static String initDatasTreeStrThird(Organ organ) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;


        secondVehicles = organ.getVehicles();
        secondVsize = secondVehicles.size();

        List<Organ> organs = organ.getOrgans();
        secondOsize = organs.size();
        if (secondVsize > 0) {
            for (VehiclesBean secondVehicle : secondVehicles) {
                stateType2 = secondVehicle.getStateType();
                countAll++;
                if (stateType2.contains("1")) {
                    count++;
                } else if (stateType2.contains("2")) {
                    count++;
                }
            }
        }


//                第三层
        if (secondOsize > 0) {
            for (Organ secondOrgans : organs) {

                thirdVehicles = secondOrgans.getVehicles();
                thirdVsize = thirdVehicles.size();

                thirdOrgans = secondOrgans.getOrgans();
                thirdOsize = thirdOrgans.size();

                if (thirdVsize > 0) {
                    for (VehiclesBean thirdVehicle : thirdVehicles) {
                        countAll++;
                        sStateType3 = thirdVehicle.getStateType();
                        if (sStateType3.contains("1")) {
                            count++;
                        } else if (sStateType3.contains("2")) {
                            count++;
                        }
                    }
                }


//                        第四层
                if (thirdOsize > 0) {
                    for (Organ thirdOrgan : thirdOrgans) {

                        fourthvehicles = thirdOrgan.getVehicles();
                        fourthVsize = fourthvehicles.size();

                        fourthOrgans = thirdOrgan.getOrgans();
                        fourthOsize = fourthOrgans.size();

                        String stateType4 = "";
                        if (fourthVsize > 0) {
                            for (VehiclesBean fourthvehicle : fourthvehicles) {
                                countAll++;
                                stateType4 = fourthvehicle.getStateType();
                                if (stateType4.contains("1")) {
                                    count++;
                                } else if (stateType4.contains("2")) {
                                    count++;
                                }
                            }
                        }

//                                第五层
                        if (fourthOsize > 0) {
                            for (Organ fourthOrgan : fourthOrgans) {

                                fifthVehicles = fourthOrgan.getVehicles();
                                fifthVsize = fifthVehicles.size();

                                fifthOrgans = fourthOrgan.getOrgans();
                                fifthOsize = fifthOrgans.size();


                                String stateType5 = "";
                                if (fifthVsize > 0) {
                                    for (VehiclesBean fifthVehicle : fifthVehicles) {
                                        countAll++;
                                        stateType5 = fifthVehicle.getStateType();
                                        if (stateType5.equals("1")) {
                                            count++;
                                        } else if (stateType5.equals("2")) {
                                            count++;
                                        }
                                    }
                                }

//                                        第六层
                                if (fifthOsize > 0) {
                                    for (Organ fifthOrgan : fifthOrgans) {
                                        sixthVehicles = fifthOrgan.getVehicles();
                                        sixthVsize = sixthVehicles.size();

                                        sixthOrganOrgans = fifthOrgan.getOrgans();
                                        sixthOsize = sixthOrganOrgans.size();

                                        String stateType6 = "";
                                        if (sixthVsize > 0) {
                                            for (VehiclesBean sixthVehicle : sixthVehicles) {
                                                stateType6 = sixthVehicle.getStateType();
                                                countAll++;
                                                if (stateType6.contains("1")) {
                                                    count++;
                                                } else if (stateType6.contains("2")) {
                                                    count++;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        countAllStr = count + "/" + countAll;
        return countAllStr;
    }


    private static String initDatasTreeStrFouth(Organ organ) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;


        secondVehicles = organ.getVehicles();
        secondVsize = secondVehicles.size();

        List<Organ> organs = organ.getOrgans();
        secondOsize = organs.size();
        if (secondVsize > 0) {
            for (VehiclesBean secondVehicle : secondVehicles) {
                stateType2 = secondVehicle.getStateType();
                countAll++;
                if (stateType2.contains("1")) {
                    count++;
                } else if (stateType2.contains("2")) {
                    count++;
                }
            }
        }


//                第三层
        if (secondOsize > 0) {
            for (Organ secondOrgans : organs) {

                thirdVehicles = secondOrgans.getVehicles();
                thirdVsize = thirdVehicles.size();

                thirdOrgans = secondOrgans.getOrgans();
                thirdOsize = thirdOrgans.size();

                if (thirdVsize > 0) {
                    for (VehiclesBean thirdVehicle : thirdVehicles) {
                        countAll++;
                        sStateType3 = thirdVehicle.getStateType();
                        if (sStateType3.contains("1")) {
                            count++;
                        } else if (sStateType3.contains("2")) {
                            count++;
                        }
                    }
                }


//                        第四层
                if (thirdOsize > 0) {
                    for (Organ thirdOrgan : thirdOrgans) {

                        fourthvehicles = thirdOrgan.getVehicles();
                        fourthVsize = fourthvehicles.size();

                        fourthOrgans = thirdOrgan.getOrgans();
                        fourthOsize = fourthOrgans.size();

                        String stateType4 = "";
                        if (fourthVsize > 0) {
                            for (VehiclesBean fourthvehicle : fourthvehicles) {
                                countAll++;
                                stateType4 = fourthvehicle.getStateType();
                                if (stateType4.contains("1")) {
                                    count++;
                                } else if (stateType4.contains("2")) {
                                    count++;
                                }
                            }
                        }

//                                第五层
                        if (fourthOsize > 0) {
                            for (Organ fourthOrgan : fourthOrgans) {

                                fifthVehicles = fourthOrgan.getVehicles();
                                fifthVsize = fifthVehicles.size();

                                fifthOrgans = fourthOrgan.getOrgans();
                                fifthOsize = fifthOrgans.size();


                                String stateType5 = "";
                                if (fifthVsize > 0) {
                                    for (VehiclesBean fifthVehicle : fifthVehicles) {
                                        countAll++;
                                        stateType5 = fifthVehicle.getStateType();
                                        if (stateType5.equals("1")) {
                                            count++;
                                        } else if (stateType5.equals("2")) {
                                            count++;
                                        }
                                    }
                                }

//                                        第六层
                                if (fifthOsize > 0) {
                                    for (Organ fifthOrgan : fifthOrgans) {
                                        sixthVehicles = fifthOrgan.getVehicles();
                                        sixthVsize = sixthVehicles.size();

                                        sixthOrganOrgans = fifthOrgan.getOrgans();
                                        sixthOsize = sixthOrganOrgans.size();

                                        String stateType6 = "";
                                        if (sixthVsize > 0) {
                                            for (VehiclesBean sixthVehicle : sixthVehicles) {
                                                stateType6 = sixthVehicle.getStateType();
                                                countAll++;
                                                if (stateType6.contains("1")) {
                                                    count++;
                                                } else if (stateType6.contains("2")) {
                                                    count++;
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        countAllStr = count + "/" + countAll;
        return countAllStr;
    }

    private static String returnAddress(final GeoCoder geoCoder, final String latTxt, final String lngTxt) {

        sLatLng = null;
        try {
            if (!TextUtils.isEmpty(latTxt) && !TextUtils.isEmpty(lngTxt)) {
                sLatLng = new LatLng(returnDouble(latTxt), returnDouble(lngTxt));
                if (geoCoder != null && sLatLng != null) {
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(sLatLng));
                    geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                        @Override
                        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

                        }

                        @Override
                        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                                address = "未知区域地址";
                            } else {
                                //获取反向地理编码结果
                                address = result.getAddress();
                            }
                        }
                    });
                } else {
                    address = "未知区域地址";
                }
            }

            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return address;
    }

    private static double returnDouble(String txt) {
        double latlng = 0;
        latlng = Double.valueOf(txt);
        return latlng;
    }

}
