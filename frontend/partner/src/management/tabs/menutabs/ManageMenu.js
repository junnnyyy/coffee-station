import React, {useState, useEffect} from 'react';
import {
  FlatList,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Image,
  Button,
} from 'react-native';
import axios from 'axios';
import styled from 'styled-components/native';


const Container = styled.View`
  flex-direction: row;
  flex: 1;
`;
const TopContainer = styled.View`
  flex-direction: row;
  margin: 10px;
  padding: 10px;
  justify-content: space-between;
  background-color: white;
`;
const ColumnContainer = styled.ScrollView`
  flex-direction: column;
  flex: 1;
  padding: 20px;
`;
const HeadButtonBox = styled.View`
  flex-direction: row;
`;
const HeadButton = styled.Pressable`
  margin: 10px;
  border-radius: 10px;
  width: 80px;
  height: 50px;
  border-width: 2px;
  justify-content: center;
  align-items: center;
`
const HeadButtonText = styled.Text`
  font-family: 'InfinitySans-Bold';
  font-size: 20px;
`
const TitleText = styled.Text`
  font-family: 'InfinitySans-Bold';
  font-size: 30px;
`
const SubText = styled.Text`
  font-family: 'InfinitySans-Bold';
  font-size: 15px;
  margin-left: 10px;
`
const WhiteBox = styled.View`
  margin: 10px;
  padding: 20px;
  background-color: white;
`
const Row = styled.View`
  flex-direction: row;
`
const ColSize = styled.View`
  flex-direction: column;
  align-items: center;
  margin: 20px;
  padding:10px;
`
const StTouchable = styled.TouchableOpacity`
  margin: 2px;
`
const StText = styled.Text`
  padding: 10px;
  font-size: 20px;
  font-family: "InfinitySans-Bold";
`
const InsideText = styled.Text`
  margin: 10px;
  font-family: "InfinitySansR";
`




const Item = ({item, onPress, backgroundColor, textColor, borderColor}) => (
  <StTouchable onPress={onPress} style={[styles.item, backgroundColor, borderColor]}>
    <StText style={[styles.title, textColor]}>{item.name}</StText>
  </StTouchable>
);

const ManageMenu = ({navigation}) => {
  const [menu, setMenu] = useState([]);
  const [selectedId, setSelectedId] = useState(null);
  const [selectedMenu, setSelectedMenu] = useState([]);
  const [sizeList, setSizeList] = useState([]);
  const [extraList, setExtraList] = useState([]);

  const selecteMenu = item => {
    setSelectedId(item.id);
    setSelectedMenu(item);
    setSizeList(item.menuSizeList.menuSizeList);
    setExtraList(item.extraList.extraList);
  };

  const renderItem = ({item}) => {
    const backgroundColor = item.id === selectedId ? '#ff7f00' : 'white';
    const color = item.id === selectedId ? 'white' : 'black';
    const borderColor = item.id === selectedId ? 'white' : '#ff7f00';

    return (
      <Item
        item={item}
        onPress={() => selecteMenu(item)}
        backgroundColor={{backgroundColor}}
        textColor={{color}}
        borderColor={{borderColor}}
      />
    );
  };
  // ?????? ????????????
  const getMenu = async () => {
    try {
      const response = await axios.get(process.env.REACT_APP_BASE_URL_PARTNER + '/menu');
      setMenu(response.data.menuList);
      // console.log(response.data.menuList[0]);
      // ?????? ????????? ?????? ???????????? ??? ???????????????
      if (response.data.menuList.length !== 0) {
        selecteMenu(response.data.menuList[0]);
      }
    } catch (e) {
      console.log(e);
    }
  };

  //?????? ?????? ??????
  const deleteMenu = async () => {
    try {
      const response = await axios.delete(
        process.env.REACT_APP_BASE_URL_PARTNER + `/menu/${selectedMenu.id}`,
      );
      console.log(response.data);
    } catch (e) {
      console.log(e);
    }
  };
  //?????? ?????? ??????
  const goCreateMenu = () => {
    navigation.navigate('CreateMenu');
  };

  useEffect(() => {
    getMenu();
  }, []);

  return (
    <Container>
      <SafeAreaView style={styles.container}>
        <FlatList
          data={menu}
          renderItem={renderItem}
          keyExtractor={item => item.id}
          extraData={selectedId}
        />
      </SafeAreaView>
      <ColumnContainer>
        <TopContainer>
          <View style={{justifyContent: 'center'}}>
            <TitleText style={{margin:10}}>????????????</TitleText>
          </View>
          <HeadButtonBox>
            <HeadButton style={{borderColor: 'red'}} onPress={() => deleteMenu()}>
              <HeadButtonText>??????</HeadButtonText>
            </HeadButton>
            <HeadButton style={{borderColor: '#ff7f00'}} onPress={() => goCreateMenu()}>
              <HeadButtonText>??????</HeadButtonText>
            </HeadButton>
          </HeadButtonBox>
        </TopContainer>

        <WhiteBox style={{alignItems:'center'}}>
          <Image
            style={{height: 200, width: 200}}
            source={{uri: selectedMenu.imgUrl}}
          />
          <HeadButtonText>{selectedMenu.name}</HeadButtonText>
          <HeadButtonText>{selectedMenu.price}???</HeadButtonText>
        </WhiteBox>

        {sizeList.length !== 0 ? (
          <>
            <WhiteBox>
              <SubText> - ????????? ??????</SubText>
              <Row>
                {sizeList.map((size, index) => (
                  <ColSize key={index}>
                    <InsideText>{size.menuSizeName}</InsideText>
                    <InsideText>{size.price}???</InsideText>
                  </ColSize>
                ))}
              </Row>
            </WhiteBox>
            <WhiteBox>
              <SubText> - ?????? ?????? ??????</SubText>
              <Row>
              {extraList.map((extra, index) => (
                <ColSize key={index}>
                  <InsideText>{extra.name}</InsideText>
                  <InsideText>{extra.price}???</InsideText>
                </ColSize>
              ))}
              </Row>
            </WhiteBox>
          </>
        ) : (
          <WhiteBox>
            <InsideText>?????? ????????? ????????????.</InsideText>
          </WhiteBox>
        )}
      </ColumnContainer>
    </Container>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 0.5,
    marginTop: StatusBar.currentHeight || 0,
  },
  item: {
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16,
    // borderRadius: 10,
    borderWidth: 0.5,
  },
  title: {
    fontSize: 25,
    fontFamily: 'InfinitySansR'
  },
});

export default ManageMenu;
