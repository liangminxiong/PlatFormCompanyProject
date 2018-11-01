package com.yuefeng.utils;

import com.baidu.mapapi.model.LatLng;
import com.common.utils.Constans;
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.features.modle.carlist.CarListSelectBean;
import com.yuefeng.personaltree.model.PersonalBean;
import com.yuefeng.personaltree.model.PersonalParentBean;
import com.yuefeng.personaltree.model.PersonalXBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class PersonalDatasUtils {

    private static final String TAG = "tag";
    private static int firstOsize;
    private static String secondId;
    private static String secondOrgShortName;
    private static String firstVRegistrationNO;
    private static int firstVsize;
    private static int secondVsize;
    private static String firstVehicleId;
    private static String secondVehicleId;
    private static String secondVehicleRegistrationNO;
    private static int secondOsize;
    private static String thirdOrgansId;
    private static String thirdOrgansOrgShortName;
    private static int thirdVsize;
    private static int thirdOsize;
    private static String thirdVehicleId;
    private static String thirdVehicleRegistrationNO;
    private static String thirdOrganId;
    private static String thirdOrganOrgShortName;
    private static int fourthVsize;
    private static int fourthOsize;
    private static String fourthvehicleId;
    private static String fourthvehicleRegistrationNO;
    private static String fourthOrganId;
    private static String fourthOrganOrgShortName;
    private static int fifthVsize;
    private static String fifthVehicleId;
    private static String fifthVehicleRegistrationNO;
    private static int fifthOsize;
    private static String fifthOrganId;
    private static String fifthOrganOrgShortName;
    private static int sixthVsize;
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
    private static String address = "";
    private static LatLng sLatLng;
    private static String sGt = "";
    private static String sSpeed = "";
    private static String sObd = "";
    private static String sTreeStr;
    private static String sStrSecond;
    private static String sStrhird;
    private static String sStrFouth;
    private static String sStrFifth;
    private static String sStrSixth;
    private static List<PersonalXBean> sFirstVehicles;

    public static List<CarListSelectBean> carListSelect(List<Node> carDatas, String keyWord) {
        List<CarListSelectBean> carListSelect = new ArrayList<>();

        for (Node carData : carDatas) {
            String id = (String) carData.getId();
            String count = carData.getCount();
            String name = carData.getName();
            String stateType = carData.getStateType();
            String terminalNO = (String) carData.getId();
            if (count.equals("0")) {
                if (name.contains(keyWord)) {
                    CarListSelectBean selectBean = new CarListSelectBean();
                    selectBean.setId(id);
                    selectBean.setName(name);
                    selectBean.setType(stateType);
                    selectBean.setTerminal(terminalNO);
                    carListSelect.add(selectBean);
                }
            }
        }


        return carListSelect;
    }


    /*人员列表 ================begin===================*/
    public static List<Node> ReturnPersonalTreesDatas(List<PersonalParentBean> msg) {

        List<Node> mDatas = new ArrayList<Node>();

        int size = msg.size();
        if (size <= 0) {
            return new ArrayList<Node>();
        }

        sTreeStr = initDatasPersonalTreeStr(msg);
        PersonalParentBean msgBean = msg.get(0);

        String orgShortName = msgBean.getOrgName();
        String fatherPid = msgBean.getPid();
        String fatherId = msgBean.getId();


        List<PersonalXBean> firstVehicles = msgBean.getPersonal();
        firstVsize = firstVehicles.size();
        List<com.yuefeng.personaltree.model.OrgansBean> firstOrgans = msgBean.getOrgans();
        firstOsize = firstOrgans.size();
        /*第一层*/
        // id , pid , label , 其他属性
        mDatas.add(new Node(fatherId, fatherPid, orgShortName, sTreeStr, "", "", "", "", "", "", false));

        if (firstVsize > 0) {
            for (PersonalXBean firstVehicle : firstVehicles) {
                firstVehicleId = firstVehicle.getId();
                firstVRegistrationNO = firstVehicle.getName();
                firstVehicleStateType = firstVehicle.getStateType();
                firstVehicleTerminalNO = firstVehicle.getTerminalNO();
                mDatas.add(new Node(firstVehicleId + "", fatherId + "", firstVRegistrationNO,
                        Constans.COUNT_ZERO, firstVehicleStateType, firstVehicleTerminalNO, sGt, sSpeed, sObd, address, false));
            }
        }


        /*第二层*/
        if (firstOsize > 0) {
            for (com.yuefeng.personaltree.model.OrgansBean firstOrgan : firstOrgans) {
                sStrSecond = initDatasPersonalTreeStrSecond(firstOrgan);

                secondId = firstOrgan.getId();
                secondOrgShortName = firstOrgan.getOrgName();


                List<PersonalBean> secondVehicles = firstOrgan.getPersonlist();
                secondVsize = secondVehicles.size();
                List<com.yuefeng.personaltree.model.Organ> organs = firstOrgan.getOrgans();
                secondOsize = organs.size();


                mDatas.add(new Node(secondId + "", fatherId + "", secondOrgShortName,
                        sStrSecond, "", "", "", "", "", "", false));


                if (secondVsize > 0) {
                    for (PersonalBean secondVehicle : secondVehicles) {
                        secondVehicleId = secondVehicle.getId();
                        secondVehicleRegistrationNO = secondVehicle.getName();
                        secondVehicleStateType = secondVehicle.getStateType();
                        secondVehicleTerminalNO = secondVehicle.getTerminalNO();
                        mDatas.add(new Node(secondVehicleId + "", secondId + "", secondVehicleRegistrationNO,
                                Constans.COUNT_ZERO, secondVehicleStateType, secondVehicleTerminalNO, sGt, sSpeed, sObd, address, false));
                    }
                }

//                第三层
                if (secondOsize > 0) {

                    for (com.yuefeng.personaltree.model.Organ organ : organs) {
                        sStrhird = initDatasPersonalTreeStrThird(organ);
                        thirdOrgansId = organ.getId();
                        thirdOrgansOrgShortName = organ.getOrgName();

                        List<PersonalBean> thirdVehicles = organ.getVideoes();
                        thirdVsize = thirdVehicles.size();
                        List<com.yuefeng.personaltree.model.Organ> thirdOrgans = organ.getOrgans();
                        thirdOsize = thirdOrgans.size();

//                        第三层总数据
                        mDatas.add(new Node(thirdOrgansId + "", secondId + "", thirdOrgansOrgShortName,
                                sStrhird, "", "", "", "", "", "", false));


                        if (thirdVsize > 0) {
                            for (PersonalBean thirdVehicle : thirdVehicles) {
                                thirdVehicleId = thirdVehicle.getId();
                                thirdVehicleRegistrationNO = thirdVehicle.getName();
                                thirdVehicleStateType = thirdVehicle.getStateType();
                                thirdVehicleTerminalNO = thirdVehicle.getTerminalNO();
                                mDatas.add(new Node(thirdVehicleId + "", thirdOrgansId + "", thirdVehicleRegistrationNO,
                                        Constans.COUNT_ZERO, thirdVehicleStateType, thirdVehicleTerminalNO, sGt, sSpeed, sObd, address, false));
                            }
                        }

//                        第四层
                        if (thirdOsize > 0) {
                            for (com.yuefeng.personaltree.model.Organ thirdOrgan : thirdOrgans) {

                                sStrFouth = initDatasPersonalTreeStrFouth(thirdOrgan);

                                thirdOrganId = thirdOrgan.getId();
                                thirdOrganOrgShortName = thirdOrgan.getOrgName();
                                List<PersonalBean> fourthvehicles = thirdOrgan.getVideoes();
                                fourthVsize = fourthvehicles.size();
                                List<com.yuefeng.personaltree.model.Organ> fourthOrgans = thirdOrgan.getOrgans();
                                fourthOsize = fourthOrgans.size();

                                mDatas.add(new Node(thirdOrganId + "", thirdOrgansId + "",
                                        thirdOrganOrgShortName, sStrFouth, "", "", "", "", "", "", false));


                                if (fourthVsize > 0) {
                                    for (PersonalBean fourthvehicle : fourthvehicles) {
                                        fourthvehicleId = fourthvehicle.getId();
                                        fourthvehicleRegistrationNO = fourthvehicle.getName();
                                        fourthvehicleStateType = fourthvehicle.getStateType();
                                        fourthvehicleTerminalNO = fourthvehicle.getTerminalNO();
                                        mDatas.add(new Node(fourthvehicleId + "", thirdOrganId + "", fourthvehicleRegistrationNO,
                                                Constans.COUNT_ZERO, fourthvehicleStateType, fourthvehicleTerminalNO, sGt, sSpeed, sObd, address, false));
                                    }
                                }

//                                第五层
                                if (fourthOsize > 0) {
                                    for (com.yuefeng.personaltree.model.Organ fourthOrgan : fourthOrgans) {

                                        sStrFifth = initDatasPersonalTreeStrFouth(fourthOrgan);

                                        fourthOrganId = fourthOrgan.getId();
                                        fourthOrganOrgShortName = fourthOrgan.getOrgName();

                                        List<PersonalBean> fifthVehicles = fourthOrgan.getVideoes();
                                        fifthVsize = fifthVehicles.size();
                                        List<com.yuefeng.personaltree.model.Organ> fifthOrgans = fourthOrgan.getOrgans();
                                        fifthOsize = fifthOrgans.size();


                                        mDatas.add(new Node(fourthOrganId + "", thirdOrganId + "",
                                                fourthOrganOrgShortName, sStrFifth, "", "", "", "", "", "", false));


                                        if (fifthVsize > 0) {
                                            for (PersonalBean fifthVehicle : fifthVehicles) {
                                                fifthVehicleId = fifthVehicle.getId();
                                                fifthVehicleRegistrationNO = fifthVehicle.getName();
                                                fifthVehicleStateType = fifthVehicle.getStateType();
                                                fifthVehicleTerminalNO = fifthVehicle.getTerminalNO();
                                                mDatas.add(new Node(fifthVehicleId + "", thirdOrganId + "", fifthVehicleRegistrationNO,
                                                        Constans.COUNT_ZERO, fifthVehicleStateType, fifthVehicleTerminalNO, sGt, sSpeed, sObd, address, false));
                                            }

                                        }

//                                        第六层
                                        if (fifthOsize > 0) {
                                            for (com.yuefeng.personaltree.model.Organ fifthOrgan : fifthOrgans) {

                                                sStrSixth = initDatasPersonalTreeStrFouth(fourthOrgan);

                                                fifthOrganId = fifthOrgan.getId();
                                                fifthOrganOrgShortName = fifthOrgan.getOrgName();
                                                List<PersonalBean> sixthVehicles = fifthOrgan.getVideoes();
                                                sixthVsize = sixthVehicles.size();
                                                List<com.yuefeng.personaltree.model.Organ> sixthOrganOrgans = fifthOrgan.getOrgans();
                                                sixthOsize = sixthOrganOrgans.size();

                                                mDatas.add(new Node(fifthOrganId + "", fourthOrganId + "",
                                                        fifthOrganOrgShortName, sStrSixth, "", "", "", "", "", "", false));


                                                if (sixthVsize > 0) {
                                                    for (PersonalBean sixthVehicle : sixthVehicles) {
                                                        sixthVehicleId = sixthVehicle.getId();
                                                        sixthVehicleRegistrationNO = sixthVehicle.getName();
                                                        sixthVehicleStateType = sixthVehicle.getStateType();
                                                        sixthVehicleTerminalNO = sixthVehicle.getTerminalNO();
                                                        mDatas.add(new Node(sixthVehicleId + "", fifthOrganId + "",
                                                                sixthVehicleRegistrationNO, Constans.COUNT_ZERO, sixthVehicleStateType,
                                                                sixthVehicleTerminalNO, sGt, sSpeed, sObd, address, false));
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


    private static String initDatasPersonalTreeStr(List<PersonalParentBean> msg) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;

        int size = msg.size();
        if (size <= 0) {
            return "";
        }
        PersonalParentBean msgBean = msg.get(0);

        /*第二层*/
        try {

            sFirstVehicles = msgBean.getPersonal();
            firstVsize = sFirstVehicles.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<com.yuefeng.personaltree.model.OrgansBean> firstOrgans = msgBean.getOrgans();
        firstOsize = firstOrgans.size();

        // id , pid , label , 其他属性

        if (firstVsize > 0) {
            for (PersonalXBean firstVehicle : sFirstVehicles) {
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
            for (com.yuefeng.personaltree.model.OrgansBean organ : firstOrgans) {
                List<PersonalBean> secondVehicles = organ.getPersonlist();
                secondVsize = secondVehicles.size();
                List<com.yuefeng.personaltree.model.Organ> organs = organ.getOrgans();
                secondOsize = organs.size();
                if (secondVsize > 0) {
                    for (PersonalBean secondVehicle : secondVehicles) {
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
                    for (com.yuefeng.personaltree.model.Organ secondOrgans : organs) {
                        List<PersonalBean> thirdVehicles = secondOrgans.getVideoes();
                        thirdVsize = thirdVehicles.size();
                        List<com.yuefeng.personaltree.model.Organ> thirdOrgans = secondOrgans.getOrgans();
                        thirdOsize = thirdOrgans.size();

                        if (thirdVsize > 0) {
                            for (PersonalBean thirdVehicle : thirdVehicles) {
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
                            for (com.yuefeng.personaltree.model.Organ thirdOrgan : thirdOrgans) {
                                List<PersonalBean> fourthvehicles = thirdOrgan.getVideoes();
                                fourthVsize = fourthvehicles.size();
                                List<com.yuefeng.personaltree.model.Organ> fourthOrgans = thirdOrgan.getOrgans();
                                fourthOsize = fourthOrgans.size();

                                String stateType4 = "";
                                if (fourthVsize > 0) {
                                    for (PersonalBean fourthvehicle : fourthvehicles) {
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
                                    for (com.yuefeng.personaltree.model.Organ fourthOrgan : fourthOrgans) {

                                        List<PersonalBean> fifthVehicles = fourthOrgan.getVideoes();
                                        fifthVsize = fifthVehicles.size();

                                        List<com.yuefeng.personaltree.model.Organ> fifthOrgans = fourthOrgan.getOrgans();
                                        fifthOsize = fifthOrgans.size();


                                        String stateType5 = "";
                                        if (fifthVsize > 0) {
                                            for (PersonalBean fifthVehicle : fifthVehicles) {
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
                                            for (com.yuefeng.personaltree.model.Organ fifthOrgan : fifthOrgans) {
                                                List<PersonalBean> sixthVehicles = fifthOrgan.getVideoes();
                                                sixthVsize = sixthVehicles.size();

                                                List<com.yuefeng.personaltree.model.Organ> sixthOrganOrgans = fifthOrgan.getOrgans();
                                                sixthOsize = sixthOrganOrgans.size();

                                                String stateType6 = "";
                                                if (sixthVsize > 0) {
                                                    for (PersonalBean sixthVehicle : sixthVehicles) {
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
        return String.valueOf(countAll);
    }

    private static String initDatasPersonalTreeStrSecond(com.yuefeng.personaltree.model.OrgansBean firstOrgans) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;


        List<PersonalBean> secondVehicles = firstOrgans.getPersonlist();
        secondVsize = secondVehicles.size();

        List<com.yuefeng.personaltree.model.Organ> organs = firstOrgans.getOrgans();
        secondOsize = organs.size();
        if (secondVsize > 0) {
            for (PersonalBean secondVehicle : secondVehicles) {
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
            for (com.yuefeng.personaltree.model.Organ secondOrgans : organs) {

                List<PersonalBean> thirdVehicles = secondOrgans.getVideoes();
                thirdVsize = thirdVehicles.size();

                List<com.yuefeng.personaltree.model.Organ> thirdOrgans = secondOrgans.getOrgans();
                thirdOsize = thirdOrgans.size();

                if (thirdVsize > 0) {
                    for (PersonalBean thirdVehicle : thirdVehicles) {
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
                    for (com.yuefeng.personaltree.model.Organ thirdOrgan : thirdOrgans) {

                        List<PersonalBean> fourthvehicles = thirdOrgan.getVideoes();
                        fourthVsize = fourthvehicles.size();

                        List<com.yuefeng.personaltree.model.Organ> fourthOrgans = thirdOrgan.getOrgans();
                        fourthOsize = fourthOrgans.size();

                        String stateType4 = "";
                        if (fourthVsize > 0) {
                            for (PersonalBean fourthvehicle : fourthvehicles) {
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
                            for (com.yuefeng.personaltree.model.Organ fourthOrgan : fourthOrgans) {

                                List<PersonalBean> fifthVehicles = fourthOrgan.getVideoes();
                                fifthVsize = fifthVehicles.size();

                                List<com.yuefeng.personaltree.model.Organ> fifthOrgans = fourthOrgan.getOrgans();
                                fifthOsize = fifthOrgans.size();

                                String stateType5 = "";
                                if (fifthVsize > 0) {
                                    for (PersonalBean fifthVehicle : fifthVehicles) {
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
                                    for (com.yuefeng.personaltree.model.Organ fifthOrgan : fifthOrgans) {
                                        List<PersonalBean> sixthVehicles = fifthOrgan.getVideoes();
                                        sixthVsize = sixthVehicles.size();

                                        List<com.yuefeng.personaltree.model.Organ> sixthOrganOrgans = fifthOrgan.getOrgans();
                                        sixthOsize = sixthOrganOrgans.size();

                                        String stateType6 = "";
                                        if (sixthVsize > 0) {
                                            for (PersonalBean sixthVehicle : sixthVehicles) {
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
        return String.valueOf(countAll);
    }


    private static String initDatasPersonalTreeStrThird(com.yuefeng.personaltree.model.Organ organ) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;


        List<PersonalBean> secondVehicles = organ.getVideoes();
        secondVsize = secondVehicles.size();

        List<com.yuefeng.personaltree.model.Organ> organs = organ.getOrgans();
        secondOsize = organs.size();
        if (secondVsize > 0) {
            for (PersonalBean secondVehicle : secondVehicles) {
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
            for (com.yuefeng.personaltree.model.Organ secondOrgans : organs) {

                List<PersonalBean> thirdVehicles = secondOrgans.getVideoes();
                thirdVsize = thirdVehicles.size();

                List<com.yuefeng.personaltree.model.Organ> thirdOrgans = secondOrgans.getOrgans();
                thirdOsize = thirdOrgans.size();

                if (thirdVsize > 0) {
                    for (PersonalBean thirdVehicle : thirdVehicles) {
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
                    for (com.yuefeng.personaltree.model.Organ thirdOrgan : thirdOrgans) {

                        List<PersonalBean> fourthvehicles = thirdOrgan.getVideoes();
                        fourthVsize = fourthvehicles.size();

                        List<com.yuefeng.personaltree.model.Organ> fourthOrgans = thirdOrgan.getOrgans();
                        fourthOsize = fourthOrgans.size();

                        String stateType4 = "";
                        if (fourthVsize > 0) {
                            for (PersonalBean fourthvehicle : fourthvehicles) {
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
                            for (com.yuefeng.personaltree.model.Organ fourthOrgan : fourthOrgans) {

                                List<PersonalBean> fifthVehicles = fourthOrgan.getVideoes();
                                fifthVsize = fifthVehicles.size();

                                List<com.yuefeng.personaltree.model.Organ> fifthOrgans = fourthOrgan.getOrgans();
                                fifthOsize = fifthOrgans.size();


                                String stateType5 = "";
                                if (fifthVsize > 0) {
                                    for (PersonalBean fifthVehicle : fifthVehicles) {
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
                                    for (com.yuefeng.personaltree.model.Organ fifthOrgan : fifthOrgans) {
                                        List<PersonalBean> sixthVehicles = fifthOrgan.getVideoes();
                                        sixthVsize = sixthVehicles.size();

                                        List<com.yuefeng.personaltree.model.Organ> sixthOrganOrgans = fifthOrgan.getOrgans();
                                        sixthOsize = sixthOrganOrgans.size();

                                        String stateType6 = "";
                                        if (sixthVsize > 0) {
                                            for (PersonalBean sixthVehicle : sixthVehicles) {
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
        return String.valueOf(countAll);
    }


    private static String initDatasPersonalTreeStrFouth(com.yuefeng.personaltree.model.Organ organ) {

        String countAllStr = "";
        int count = 0;
        int countAll = 0;

        List<PersonalBean> secondVehicles = organ.getVideoes();
        secondVsize = secondVehicles.size();
        List<com.yuefeng.personaltree.model.Organ> organs = organ.getOrgans();
        secondOsize = organs.size();
        if (secondVsize > 0) {
            for (PersonalBean secondVehicle : secondVehicles) {
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
            for (com.yuefeng.personaltree.model.Organ secondOrgans : organs) {

                List<PersonalBean> thirdVehicles = secondOrgans.getVideoes();
                thirdVsize = thirdVehicles.size();

                List<com.yuefeng.personaltree.model.Organ> thirdOrgans = secondOrgans.getOrgans();
                thirdOsize = thirdOrgans.size();

                if (thirdVsize > 0) {
                    for (PersonalBean thirdVehicle : thirdVehicles) {
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
                    for (com.yuefeng.personaltree.model.Organ thirdOrgan : thirdOrgans) {

                        List<PersonalBean> fourthvehicles = thirdOrgan.getVideoes();
                        fourthVsize = fourthvehicles.size();

                        List<com.yuefeng.personaltree.model.Organ> fourthOrgans = thirdOrgan.getOrgans();
                        fourthOsize = fourthOrgans.size();

                        String stateType4 = "";
                        if (fourthVsize > 0) {
                            for (PersonalBean fourthvehicle : fourthvehicles) {
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
                            for (com.yuefeng.personaltree.model.Organ fourthOrgan : fourthOrgans) {

                                List<PersonalBean> fifthVehicles = fourthOrgan.getVideoes();
                                fifthVsize = fifthVehicles.size();

                                List<com.yuefeng.personaltree.model.Organ> fifthOrgans = fourthOrgan.getOrgans();
                                fifthOsize = fifthOrgans.size();


                                String stateType5 = "";
                                if (fifthVsize > 0) {
                                    for (PersonalBean fifthVehicle : fifthVehicles) {
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
                                    for (com.yuefeng.personaltree.model.Organ fifthOrgan : fifthOrgans) {
                                        List<PersonalBean> sixthVehicles = fifthOrgan.getVideoes();
                                        sixthVsize = sixthVehicles.size();

                                        List<com.yuefeng.personaltree.model.Organ> sixthOrganOrgans = fifthOrgan.getOrgans();
                                        sixthOsize = sixthOrganOrgans.size();

                                        String stateType6 = "";
                                        if (sixthVsize > 0) {
                                            for (PersonalBean sixthVehicle : sixthVehicles) {
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
        return String.valueOf(countAll);
    }

    /*人员列表 ================end===================*/

}
